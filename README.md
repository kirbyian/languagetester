# LanguageTester

LanguageTester is a Spring Boot–based backend service for a **language learning platform**.  
It provides secure APIs (protected by Okta authentication) for managing vocabulary, quizzes, audio generation, and verb testing — automating much of the language content creation pipeline.

---

## 🚀 Core Features

### 🗣️ Audio Builder
- Automatically generates MP3 pronunciation files for new vocabulary items.  
- Integrates with a text-to-speech (TTS) API to produce natural-sounding speech.  
- Stores audio files in AWS S3 and links them to vocabulary entries.  
- Skips regeneration if audio already exists.

### 📚 Vocabulary Manager
- Parses CSV files containing vocabulary items (`word, translation, category, languageCode`).  
- Persists items into a database and associates each with an S3-hosted audio file.  
- Supports multiple languages through a `LanguageRepository` lookup.  
- Returns structured vocabulary data to client applications.

### 🧠 Quiz Engine
- Exposes endpoints to generate adaptive quizzes based on saved vocabulary items.  
- Supports multiple question formats (e.g., word-to-translation, audio-to-word).  
- Tracks accuracy to allow personalized difficulty scaling.

### 🔤 Verb Tester
- Provides verb conjugation practice and validation endpoints.  
- Uses rule-based logic per language to assess user answers.  
- Returns correct conjugation forms for learning feedback.

---

## 🔐 Authentication
All API routes are protected using **Okta OAuth 2.0** / **OpenID Connect**:
- Each request must include a valid Okta-issued access token.
- Integrated with Spring Security to manage login, token validation, and session state.

---

## ☁️ Technical Overview
- **Backend:** Spring Boot, Java 17, Spring Data JPA  
- **Database:** JPA-compatible (e.g., PostgreSQL, MySQL)  
- **Cloud Storage:** AWS S3 via internal `S3Service`  
- **Text-to-Speech Integration:** External API for audio generation  
- **Auth:** Okta (OAuth 2.0 / OIDC)  
- **Deployment:** Configurable via environment variables or `application.properties`

---

## 🧩 Example Flow
1. Authenticated user (via Okta) uploads or triggers a CSV of vocabulary items.  
2. The system validates and stores new words in the database.  
3. Missing audio files are generated via the TTS API and uploaded to S3.  
4. Learners access vocabulary lists, quizzes, and verb tests through frontend clients consuming this API.

---

## ⚙️ Configuration
Example environment variables:

```properties
# SpeechGen API
speechgen.api.url=https://speechgen.io/index.php?r=api/text
speechgen.api.token=${SPEECHGEN_API_TOKEN}
speechgen.api.email=${SPEECHGEN_API_EMAIL}

# AWS
aws.s3.audio.base-url=https://your-bucket.s3.eu-west-1.amazonaws.com/

# Okta
okta.oauth2.issuer=https://your-okta-domain.okta.com/oauth2/default
okta.oauth2.client-id=${OKTA_CLIENT_ID}
okta.oauth2.client-secret=${OKTA_CLIENT_SECRET}
