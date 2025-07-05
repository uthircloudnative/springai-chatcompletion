# Spring AI ChatCompletion (Vertex AI Gemini)

This project is a Spring Boot REST API that integrates with Google Vertex AI Gemini for chat completion using the Spring AI library.

## Prerequisites
- Java 24 or higher
- Maven
- Google Cloud account with Vertex AI access
- Google Cloud project and credentials (application-default credentials)

## Features
- Exposes a REST endpoint to interact with Vertex AI Gemini LLM.
- Clean separation of controller and service layers.
- Easily configurable via `application.properties`.

## How It Works

### 1. REST Endpoint

The main endpoint is:

```
GET /api/v1/vertexai/chat?prompt=your_message
```
- `prompt` is the user’s input to the LLM.
- The response is a JSON object with the generated message.

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
}
```

### 3. Service Layer
Calls the Vertex AI Gemini model using Spring AI:

```java
@Service
public class VertexAIChatService {
    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;
    public VertexAIChatService(VertexAiGeminiChatModel vertexAiGeminiChatModel) {
        this.vertexAiGeminiChatModel = vertexAiGeminiChatModel;
    }
    public Map<String, String> chat(String prompt) {
        return Map.of("message", vertexAiGeminiChatModel.call(prompt));
    }
}
```
  - Here we are using VertexAiGeminiChatModel.call() method which will act as blocking call.
  - When request is triggered to LLM it will wait the complete response to be received before giving response back to client.

### 4. Configuration
Set your Google Cloud project and model details in `src/main/resources/application.properties`:

```properties
spring.ai.model.chat=vertexai
spring.ai.vertex.ai.gemini.project-id=<Google Studio Project ID>
spring.ai.vertex.ai.gemini.location=us-central1
spring.ai.vertex.ai.gemini.chat.options.model=gemini-2.0-flash
spring.ai.vertex.ai.gemini.chat.options.temperature=0.1
```

## Running the Project
1. Set up your Google Cloud credentials and project. Create a Google Cloud or Google AI studio account if not having one.
2. Once Google account is created for local development we need to Authenticate by login to Google Account using Google CLI.
3. Once Google Account and Google CLI is installed execute below command in the local machine to get authenticated.
```
    gcloud config set project <PROJECT_ID> 
    gcloud auth application-default login <ACCOUNT>
```
4. Configure `application.properties` with your project details.
5. Start the Spring Boot application:
   ```
   ./mvnw spring-boot:run
   ```
4. Test the endpoint:
   ```
   curl "http://localhost:8080/api/v1/vertexai/chat?prompt=Give History of Java"
   ```

## Key Points
- Uses Spring AI’s `VertexAiGeminiChatModel` for LLM calls.
- All LLM logic is encapsulated in the service layer.
- Easily extendable for more endpoints or advanced LLM options.

