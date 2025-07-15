# Spring AI ChatCompletion (Vertex AI Gemini & OpenAI)

This project is a Spring Boot REST API that integrates with both Google Vertex AI Gemini and OpenAI GPT models for chat completion using the Spring AI library.

## Prerequisites
- Java 24 or higher
- Maven
- Google Cloud account with Vertex AI access
- OpenAI account and API key
- Google Cloud project and credentials (application-default credentials)

## Features
- Exposes blocking and streaming REST endpoints for both Vertex AI Gemini and OpenAI GPT models.
- Clean separation of controller and service layers.
- Easily configurable via `application.properties` for multiple LLM providers.

## How It Works

### 1. REST Endpoints

#### VertexAI Endpoints
- **Blocking:**
  ```
  GET /api/v1/vertexai/chat?prompt=your_message
  ```
- **Streaming:**
  ```
  GET /api/v1/vertexai/chat/stream?prompt=your_message
  ```

#### OpenAI Endpoints
- **Blocking:**
  ```
  GET /api/v1/openai/chat?prompt=your_message
  ```
- **Streaming:**
  ```
  GET /api/v1/openai/chat/stream?prompt=your_message
  ```

- Streaming endpoints use NDJSON (newline-delimited JSON) for real-time client consumption.

**Example usage with curl:**
```sh
curl --no-buffer "http://localhost:8080/api/v1/vertexai/chat/stream?prompt=your_message"
curl --no-buffer "http://localhost:8080/api/v1/openai/chat/stream?prompt=your_message"
```

### 2. Controller Layer
Handles HTTP requests and delegates to the service:

```java
@RestController
@RequestMapping("/api/v1")
public class VertexAIChatController {
    private final VertexAIChatService vertexAIChatService;
    public VertexAIChatController(VertexAIChatService vertexAIChatService) {
        this.vertexAIChatService = vertexAIChatService;
    }
    @GetMapping("/vertexai/chat")
    public Map<String, String> chat(@RequestParam(value="prompt") String prompt) {
        return vertexAIChatService.chat(prompt);
    }
    @GetMapping(value="/vertexai/chat/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> chatStream(@RequestParam(value="prompt") String prompt ){
        return vertexAIChatService.chatStream(prompt);
    }
}

@RestController
@RequestMapping("/api/v1")
public class OpenAIChatController {
    private final OpenAIChatService openAIChatService;
    public OpenAIChatController(OpenAIChatService openAIChatService) {
        this.openAIChatService = openAIChatService;
    }
    @GetMapping("/openai/chat")
    public Map<String, String> chat(@RequestParam(value="prompt") String prompt) {
        return openAIChatService.chat(prompt);
    }
    @GetMapping(value = "/openai/chat/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> chatStream(@RequestParam(value="prompt") String prompt) {
        return openAIChatService.chatStream(prompt);
    }
}
```

### 3. Service Layer
Calls the respective LLM model using Spring AI:

```java
@Service
public class VertexAIChatService {
    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;
    public VertexAIChatService(VertexAiGeminiChatModel vertexAiGeminiChatModel) {
        this.vertexAiGeminiChatModel = vertexAiGeminiChatModel;
    }
    // Blocking call
    public Map<String, String> chat(String prompt) {
        return Map.of("message", vertexAiGeminiChatModel.call(prompt));
    }
    // Streaming call
    public Flux<String> chatStream(String prompt) {
        UserMessage userMessage = new UserMessage(prompt);
        Prompt userPrompt = Prompt.builder()
                                  .messages(userMessage)
                                  .build();
        return vertexAiGeminiChatModel.stream(userPrompt)
                                      .map(chatresponse -> chatresponse.getResult().getOutput().getText());
    }
}

@Service
public class OpenAIChatService {
    private final OpenAiChatModel openAiChatModel;
    public OpenAIChatService(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }
    // Blocking call
    public Map<String, String> chat(String prompt) {
        return Map.of("message", openAiChatModel.call(prompt));
    }
    // Streaming call
    public Flux<String> chatStream(String prompt) {
        UserMessage userMessage = new UserMessage(prompt);
        Prompt userPrompt = Prompt.builder()
                                  .messages(userMessage)
                                  .build();
        return openAiChatModel.stream(userPrompt)
                              .map(chatresponse -> chatresponse.getResult().getOutput().getText());
    }
}
```
- Both services provide blocking and streaming methods for their respective models.

### 4. Configuration
Set your Google Cloud and OpenAI project/model details in `src/main/resources/application.properties`:

```properties
# VertexAI
spring.ai.vertex.ai.gemini.project-id=<Google Studio Project ID>
spring.ai.vertex.ai.gemini.location=us-central1
spring.ai.vertex.ai.gemini.chat.options.model=gemini-2.0-flash
spring.ai.vertex.ai.gemini.chat.options.temperature=0.1

# OpenAI
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.base-url=https://api.openai.com
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.2
```
- Do **not** set `spring.ai.model.chat` to allow multiple models.
- Pass the OpenAI API key as an environment variable:
  ```sh
  export OPENAI_API_KEY=your_actual_openai_key
  ./mvnw spring-boot:run
  ```

## Running the Project
1. Set up your Google Cloud and OpenAI credentials/accounts.
2. Authenticate with Google Cloud using Google CLI:
   ```
   gcloud config set project <PROJECT_ID>
   gcloud auth application-default login <ACCOUNT>
   ```
3. Configure `application.properties` with your project details.
4. Start the Spring Boot application:
   ```
   ./mvnw spring-boot:run
   ```
5. Test the endpoints:
   - VertexAI Blocking:
     ```
     curl "http://localhost:8080/api/v1/vertexai/chat?prompt=Give History of Java"
     ```
   - VertexAI Streaming:
     ```
     curl --no-buffer "http://localhost:8080/api/v1/vertexai/chat/stream?prompt=Give History of Java"
     ```
   - OpenAI Blocking:
     ```
     curl "http://localhost:8080/api/v1/openai/chat?prompt=Give History of Java"
     ```
   - OpenAI Streaming:
     ```
     curl --no-buffer "http://localhost:8080/api/v1/openai/chat/stream?prompt=Give History of Java"
     ```

## Key Points
- Supports both Google Vertex AI Gemini and OpenAI GPT models in a single application.
- All LLM logic is encapsulated in the service layer for each provider.
- Blocking and real-time streaming endpoints for flexible client integration.
- Easily extendable for more endpoints or advanced LLM options.
- Environment variable support for sensitive keys (OpenAI API key).

---
This README provides a quick technical overview and helps you get started or extend the project further.
