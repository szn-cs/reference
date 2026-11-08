## [[2026-02-07]]

- > OpenRouter, Together AI, Fireworks AI, Groq, Replicate;
- Databricks
- Venice.ai
- Snowflake
- [[Tech Stack/AI, LLMs, Agents/LLMs/journal#^update-subscription-of-glm-4-7]] through Z.ai provider

## [[2026-02-07]]

- _info:
    - _OpenRouter, DeepSeek, Ollama, Gemini, Volcengine, and SiliconFlow, etc._
    - _OpenAI-compatibe API is the standard for LLM provider support_
    - _Groq, Cerebras, and SambaNova - speed demons_
    - _Together, Fireworks, Inference, Novita, and Deepinfra;_
    - Low-latency providers are burst providers (not for large quantity of data & repetitive calls), not bulk providers; The tradeoff is between **bulk efficiency (cost/throughput)** and **Low-latency**; In addition to flexibility (model choice);
    - _LiteLLM, Kong AI Gateway, and llmgateway.io, portKey_
      _OpenRouter  Helicone Cloud  Cloudflare AI Gateway  Eden AI  LiteLLM  Portkey  Kong AI Gateway_
        - Permits centralized control & usage managements at the gateway layer while requiring upfront integration with each provider's API credentials/billing; i.e. they do not consolidate billing for the platform gateway operator (no elastic usage but requires to setup keys for each LLM subscription).
        - Generates virtual keys for users to use and track their usage.
        - these solutions still require separate keys and subscriptions to each of the LLM providers.
        - [https://shareai.now/](https://shareai.now/)
- Main coding tasks competitors: Claude Sonnet 4.5; GLM; Kimi K2; Qwen3-Coder; ChatGPT5 ^main-coding-tasks-competitors-claude-sonnet-4-5-glm-kimi-k2
    - Deepseek v3.1 - cheap and great for reasoning. (check providers - maybe at groq?)
    - chinease: Zhipu AI, DeepSeek, O1.AI, Moonshot AT
    - Qwen3 235B Instruct or Deepseek V3.2!
- **Together.ai**
    - [https://www.together.ai/models](https://www.together.ai/models)
- Groq [https://console.groq.com/dashboard](https://console.groq.com/dashboard) - crazy speed, Groq allows for quick inferences by providing many LLMs
    - [https://community.groq.com/](https://community.groq.com/)
    - [https://console.groq.com/docs/models](https://console.groq.com/docs/models)
        - Model combination system offerings [https://console.groq.com/docs/models#production-systems](https://console.groq.com/docs/models#production-systems)
    - [https://groq.com/pricing](https://groq.com/pricing)
    - Open source app generation project based on Groq provider (official project) [https://appgen.groqlabs.com/](https://appgen.groqlabs.com/)  &  [https://github.com/groq/groq-appgen](https://github.com/groq/groq-appgen)
- DeepInfra (extremely cheap provider)
    - [https://deepinfra.com/pricing](https://deepinfra.com/pricing)
- [https://www.siliconflow.com/models](https://www.siliconflow.com/models)
    - [https://www.siliconflow.com/pricing](https://www.siliconflow.com/pricing)
- Qwen (Alibaba)
- **Kimi K2**: China Moonshot [https://www.moonshot.ai/](https://www.moonshot.ai/)
    - [https://platform.moonshot.ai/docs/overview](https://platform.moonshot.ai/docs/overview)
    - [https://www.kimi.com/](https://www.kimi.com/) Kimi K2: rival to Claude Code LLMs
        - [https://moonshotai.github.io/Kimi-K2/](https://moonshotai.github.io/Kimi-K2/)
        - Pricing [https://platform.moonshot.ai/docs/pricing/chat#product-pricing](https://platform.moonshot.ai/docs/pricing/chat#product-pricing)
- z.ai models [https://z.ai/blog/glm-4.5](https://z.ai/blog/glm-4.5)
    - WARNING: issue with subscription cancellation.
    - GLM LLMs are very cheap by Zhipu AI, Beijing;
        - cons: No deep reasoning mode; web search doesn't work;  less polished output; significant privacy trade-off;
    - performance bench [https://z.ai/blog/glm-4.5](https://z.ai/blog/glm-4.5)
    - [https://docs.z.ai/scenario-example/develop-tools/claude#step-3%3A-quick-start-with-claude-code](https://docs.z.ai/scenario-example/develop-tools/claude#step-3%3A-quick-start-with-claude-code)
    - [https://open.bigmodel.cn/dev/api/normal-model/glm-4](https://open.bigmodel.cn/dev/api/normal-model/glm-4)
- China free models [https://platform.iflow.cn/en/models](https://platform.iflow.cn/en/models)
- Gemini, Ollama, OpenAI, Anthropic
- [https://aihubmix.com/](https://aihubmix.com/) chinease competitor to OpenRouter
- [OpenRouter](https://openrouter.ai/)  (offers access to AI models - unifying endpoint)
    - **Unified Billing** and access to **Multiple LLM Providers** through a **Single API**
    - [https://openrouter.ai/docs/features/provider-routing](https://openrouter.ai/docs/features/provider-routing)
    - [https://openrouter.ai/moonshotai/kimi-k2-0905](https://openrouter.ai/moonshotai/kimi-k2-0905)
    - [verify that this is the case] usually has a 5.5% fee on the credits purchased only; while passing through the pricing of the underlying model providers (no price change per token).
- [https://www.edenai.co/pricing](https://www.edenai.co/pricing)
- [https://portkey.ai/pricing](https://portkey.ai/pricing)
- [https://www.hyperbolic.ai/inference](https://www.hyperbolic.ai/inference)
- [https://www.requesty.ai/solution/llm-routing/models?providers=zai](https://www.requesty.ai/solution/llm-routing/models?providers=zai)
- [https://novita.ai/pricing](https://novita.ai/pricing)
- [CometAPI](https://www.cometapi.com/) (offers access to multiple AI models)
- [https://www.litellm.ai/](https://www.litellm.ai/) opensource LLM router
    - [https://docs.litellm.ai/docs/](https://docs.litellm.ai/docs/)
    - setup with claude code router [https://docs.litellm.ai/docs/tutorials/claude_responses_api](https://docs.litellm.ai/docs/tutorials/claude_responses_api)
- [https://github.com/helicone/ai-gateway/](https://github.com/helicone/ai-gateway/) opensource rust based
- [https://chutes.ai/](https://chutes.ai/)
- [https://llmgateway.io/](https://llmgateway.io/)     - opensource/self-hosted LLM gateway.
- [https://github.com/katanemo/archgw](https://github.com/katanemo/archgw)
- [https://github.com/maximhq/bifrost](https://github.com/maximhq/bifrost)
- [https://github.com/tensorzero/tensorzero](https://github.com/tensorzero/tensorzero)
- LLAMA
- > MiniMax, Synthetic, Venice AI, OpenRouter, Vercel AI Gateway, Moonshot AI, Z.AI (GLM 4.7), OpenCode Zen.
- **Claude Code LLM models providers**
  Proprietary models such as Claude Code models can be provided by thirdparty cloud providers like Amazon, Google, Azure etc. ^claude-code-llm-models-providers
    - Antropic AI provider (owner of Claude Code)
    - Google Vertex AI
        - provides Claude Code models
    - Amazon AWS Bedrock
        - providers Claude Code models
    - **Microsoft Foundry** [https://azure.microsoft.com/en-us/blog/introducing-claude-opus-4-5-in-microsoft-foundry/](https://azure.microsoft.com/en-us/blog/introducing-claude-opus-4-5-in-microsoft-foundry/)
- | Provider | Limits | Models | Link |
  |----------|--------|--------|------|
  | OpenRouter | 20 requests/min; 50 requests/day; 1000 requests/day with $10 top-up | DeepCoder 14B Preview; DeepHermes 3; DeepSeek R1; Llama 3.x; Mistral 7B; Qwen 2.5; Gemma 3; ... | [https://openrouter.ai/](https://openrouter.ai/) |
  | Google AI Studio | 10–30 requests/min; 500–1000 requests/day (depending on model) | Gemini 1.5/2.0 Flash; Gemini 2.5; Gemma 3; LearnLM; text-embedding | [https://makersuite.google.com/](https://makersuite.google.com/) |
  | NVIDIA NIM | 40 requests/min | Various open models | [https://developer.nvidia.com/nim](https://developer.nvidia.com/nim) |
  | Mistral (La Plateforme) | 1 req/sec; 500k tokens/min; 1B tokens/month | Open/proprietary Mistral models | [https://la.mistral.ai/](https://la.mistral.ai/) |
  | Mistral (Codestral) | 30 requests/min; 2000 requests/day | Codestral | [https://codestral.com/](https://codestral.com/) |
  | HuggingFace Inference Providers | $0.10/month credits | Various open models (< 10GB) | [https://huggingface.co/inference-endpoints](https://huggingface.co/inference-endpoints) |
  | Cerebras | 30 req/min; 60,000 tokens/min; 900 req/hr; 14,400 req/day | Qwen 3 32B; Llama 3.1/3.3/4 Scout; others | [https://cerebras.net/](https://cerebras.net/) |
  | Groq | model-specific; e.g., 7000 req/day & 6000 tokens/min | Allam 2 7B; DeepSeek R1 Distill 70B; Whisper Large; Gemma 2; Mistral Saba 24B; Llama 3/3.1/4; Qwen QwQ 32B; ... | [https://groq.com/](https://groq.com/) |
  | Together | 60 req/min | Llama 3.2 Vision; Llama 3.3; DeepSeek R1 Distill 70B | [https://www.together.ai/](https://www.together.ai/) |
  | Cohere | 20 req/min; 1000 req/month | Command-A/R/R7B/R+; Aya Expanse 8B/32B; Aya Vision 8B/32B | [https://cohere.com/](https://cohere.com/) |
  | byokaashish | [https://www.feedough.com/subscribe/](https://www.feedough.com/subscribe/) | www.linkedin.com/in/aashishpahwa/ | [https://www.instagram.com/okaashish/](https://www.instagram.com/okaashish/) |
