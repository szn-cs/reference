# AI Coding Agent Framework SDKs / AI Agent Development Frameworks
id:: 691a7628-a564-457c-887f-95fd18109e74
tools, APIs, and abstractions to build, orchestrate, and deploy intelligent autonomous AI agents and applications. Abstractions over LLMs, multi-agent orchestration, sandboxed execution (some SDK often also have infrastructure capability), extensibility. These SDKs provide the logic, state management, and orchestration for agents that write code (reasoning loop, memory, and decision-making).
	- description
	  collapsed:: true
		- features include: multi-model orchestration, tool integrations, dynamic workflow control, real-time streaming, sandboxed execution, and extensible agent behaviors.
		- SDKs provide complex usecases and save development time.
		- No-code platforms rely on combination of a **Control Plane** (Brain), a **Sandboxing Layer** (Computer), and **Tooling Protocols** (Hands).
	- **Langchain**
	  collapsed:: true
		- allows developers to define **loops** (Write → Error → Fix → Retry) cyclical flows explicitly as a graph, rather than a straight chain;
		- Python/JS
		- used by **Replit's** "Breakout Agents" architecture.
	- LangGraph
	- **Vercel AI SDK**
		- **"Streaming UI."** It allows the agent to stream code directly into a React component as it is being written, preventing the "spinner of death" while the user waits for a full file to generate.
		- TypeScript
		- used by **Bolt.new**, **Lovable.dev**
	- **OpenAI SDK**
	- ==**Claude Code Agent SDK**==
	  id:: 6924424c-ea4e-4196-b016-fff6ac070055
		- gives the LLM direct, native control over a local terminal and file system; optimized for the **Claude 3.5 Sonnet feedback loop** (Think → Act → Observer → Repeat).
		- The agent SDK allows to take claude code and create a highly specialized agent system for your particular usecase.
		- #+BEGIN_QUOTE
		  - Function & Key Features: Anthropic's Native SDK. The underlying engine for the "Claude Code" CLI. It provides an Agent primitive that natively understands file systems, bash commands, and git
		  - **Stack**: **TypeScript/Python SDKs**, built on **Claude** models, utilizes a secure **Code Execution Tool** (sandbox), and integrates native **terminal/bash** access.
		  #+END_QUOTE
		- cons: developer hands over control to the library implementation of event loop and function calling, as opposed to building from scratch.
		- RUST SDK ?
			- https://github.com/yafatek/rust-deep-agents-sdk
			- https://github.com/Wally869/claude_agent_sdk_rust
			- https://github.com/modelcontextprotocol/rust-sdk/tree/main
			- Write rust functions and compile them for nodejs modules
				- Q: How slow is the communication layer between rust and javascript runtime compared to pure javascript implementation?
	- Multi-agent AI system SDK [https://github.com/strands-agents/sdk-python](https://github.com/strands-agents/sdk-python)
	- **Microsoft Agent Framework** (AutoGen)
		- Enterprise internal tools (.NET/Azure ecosystems).
		- #+BEGIN_QUOTE
		  - Function & Key Features: Enterprise Orchestration. Microsoft's evolving standard for building agents that work within the .NET/Python ecosystem. It excels at **multi-agent delegation** (e.g., a "Coder" agent handing off to a "Reviewer" agent).
		  - **Stack**: **Python** (3.10+) and **.NET/C#**, supports **Azure OpenAI/OpenAI**, built on **Graph-based orchestration** (Workflows) and concepts from AutoGen.
		  #+END_QUOTE
	- **Factory.ai SDK**
	  collapsed:: true
		- #+BEGIN_QUOTE
		  - Function & Key Features: "Droids" Automation. Focuses on "Agent-Native Software Development." Allows building "Droids" that integrate directly into **CI/CD pipelines** and treats coding tasks as autonomous background jobs.
		  - **Stack**: **API-first** architecture, **Model-agnostic** agent design (Opus, GPT-5, etc.), delivered via a **CLI** and specialized agent personas (Droids).
		  #+END_QUOTE
	- https://google.github.io/adk-docs/
	  collapsed:: true
		- gemini support https://developers.googleblog.com/building-ai-agents-with-google-gemini-3-and-open-source-frameworks/
	- ### Auxiliary SDKs augmenting AI agent with capabilities and MCPs - Deep Research & Web Browsing SDKs for AI Agents:
		- Overview: Tools that empower agents to interact with the live internet for data gathering and report generation.
		- **MCP Rust SDK**
		- MCP tools for connecting to postgresql or linear
		- **Restate SDK**
		  collapsed:: true
			- provides durable execution allowing to resume on another instance while retaining all variable state (memory)
			- #+BEGIN_QUOTE
			  - Function & Key Features: Durable Execution. **Crucial for production agents.** Ensures the AI agent resumes exactly where it left off without losing variable state if it crashes or times out. It wraps Vercel AI SDK or OpenAI agents.
			  - **Stack**: SDKs available in **TypeScript/Node.js**, **Java/Kotlin**, and **Python**. The core is a resilient, **Durable Execution** runtime.
			  #+END_QUOTE
		- https://www.agentops.ai/
		- #+BEGIN_QUOTE
		    - **GPT Researcher**
		      - Category: Agent Framework
		      - Function: Best for Automated Research Reports, synthesizing a final, cited report using a Planner and Execution agent model.
		      - **Stack**: **Python**, **LangChain/LangGraph** (for orchestration), **Tavily API** (default search), leverages multi-agent collaboration.
		    - **Browser-Use**
		      - Category: Browser Control
		      - Function: Excels at Human-like Web Interaction by connecting the LLM directly to the browser's Accessibility Tree to "see" elements and perform native actions.
		      - **Stack**: **Python** (>=3.11), relies on **Chrome DevTools Protocol (CDP)**, integrates with multiple LLM clients (OpenAI, Anthropic, etc.).
		    - **Browserbase**
		      - Category: Headless Infrastructure
		      - Function: Best for High-Volume and Reliable Scraping, providing secure, cloud-hosted browser environments with anti-detection features.
		      - **Stack**: **Node.js/Python SDKs**, **Serverless Chromium** (Cloud), compatible with **Playwright**, **Puppeteer**, and **Stagehand** SDK.
		    - **Firecrawl**
		      - Category: Data Preparation
		      - Function: Essential for Converting Web to LLM Text (RAG); crawls sites and converts messy HTML/PDFs into clean, structured Markdown.
		      - **Stack**: **API-first** service, official SDKs in **Python** and **Node.js**, integrates with **LangChain**, **Llama Index**, and **CrewAI**.
		    - **Tavily**
		      - Category: Search API
		      - Function: Ideal for Focused Search Results as it is an AI-optimized search engine that returns only the relevant text snippets (context) directly to the agent.
		      - **Stack**: **REST API**, official SDKs in **Python** and **Node.js**, designed for direct RAG pipelines and **LangChain** integration.
		  #+END_QUOTE
	- infrastructure ((691e7a58-3222-45a0-aca0-c74e5fae627d))
	-
	- ### Conclusion
		- ==Claude Code Agent SDK== handles the event loop and tool calling including built-in features. Permits creating coding agents as well as general purpose agent systems.
		- Q: is Vercel SDK needed for streaming responses in browser?
-
- # Coding agents tools:
  collapsed:: true
	- info:
		- _24 million tokens/day** —enough for 3–4 hours of uninterrupted vibe coding_ while _heavy coding workflows up to 120m tokens/day_
	- Groq allows for quick inferences by providing many LLMs
	- List of coding agents
		- https://models.dev/
		- https://huggingface.co/models
		- https://models.litellm.ai/
		- performance analysis https://artificialanalysis.ai/
			- https://artificialanalysis.ai/leaderboards/models
			- https://artificialanalysis.ai/models/capabilities/coding
		- provides a good list of models and fileters https://deepinfra.com/models
		- https://github.com/cheahjs/free-llm-api-resources
		- China opensource community https://modelscope.cn/home
-
	- ## Claude Code
	  id:: 68d89560-cf08-435a-acb8-1a62c224016f
		- info :
			- accessible through CLI or SDK.
				- thirdparty providers may allow free tier access (Google, Amazon bedrock)
			- specific phrases are mapped directly to increasing levels of thinking budget in the system: "think" < "think hard" < "think harder" < "ultrathink." Each level allocates progressively more thinking budget for Claude to use.
			- subagents allow to modify the system prompt but has shortcoming in other places.
			- Claude API pay-as-you-go is a lot more expensive  than the subscription-based when used heavily.
			- Claude code is able ot keep track of progress in working scratchpad.
			- Claude Code supports headless mode for non-interactive contexts
		- https://claude.com/product/claude-code
		- Prompt improver https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/prompt-improver
		- https://docs.claude.com/en/api/agent-sdk/overview - Python + TS/JS
		- https://claudelog.com/
		- SDK agents https://docs.claude.com/en/docs/claude-code/sdk/sdk-overview
			- API interface to access Claude Code grammatically.
			- Some tools follow the Agent Client Protocal https://agentclientprotocol.com/overview/introduction
		- Intercepts Claude Code cli communication and redirects to different LLMs https://github.com/musistudio/claude-code-router
			- docs https://github.com/musistudio/claude-code-router
			- config example https://github.com/musistudio/claude-code-router/blob/main/ui/config.example.json
			- blog articles https://github.com/musistudio/claude-code-router/tree/main/blog/en
			- cheatsheet https://awesomeclaude.ai/code-cheatsheet
		- #+BEGIN_QUOTE
		  "Run multiple Task invocations in a SINGLE message" to run in parallel 
		  #+END_QUOTE
		- TODO https://github.com/ericbuess/claude-code-docs
		- TODO https://github.com/zilliztech/claude-context   or   https://github.com/ericbuess/claude-code-project-index
		- TODO checkout tool MCP task management system for https://github.com/eyaltoledano/claude-task-master
		- ### Plugins marketplace
		  id:: 691a686b-a672-4b01-8c7f-651cd9c7f6f9
			- https://github.com/wshobson/agents
			- https://claudecodemarketplace.com/
			- https://github.com/edmund-io/edmunds-claude-code from [youtuber](https://www.youtube.com/watch?v=Ffh9OeJ7yxw)
		- ### MCP :
			- https://context7.com/
				- DONE install https://github.com/upstash/context7
			- DONE https://github.com/21st-dev/magic-mcp create magic interfaces
			- TODO https://ref.tools/
			  id:: 691a98a6-81b6-460c-8404-5fa43d652db4
			- TODO https://exa.ai/
			  id:: 691a98a8-5137-4d73-9305-6dfde655e88b
			- DONE https://docs.firecrawl.dev/mcp-server mcp
			  id:: 691b2a3c-da7e-4369-a995-5bbac0d380b3
			- TODO https://help.figma.com/hc/en-us/articles/32132100833559-Guide-to-the-Figma-MCP-server
			- ### Resources
				- https://mcpservers.org/
				- https://github.com/modelcontextprotocol/servers
				- https://mcpmarket.com/
		- ### Subagents
			- https://github.com/lst97/claude-code-sub-agents/tree/main/agents examples (from [youtuber](https://www.youtube.com/@edmundyong))
		- ### Resources
			- TODO [I should be charging $999 for this Claude Code Tutorial](https://www.youtube.com/watch?v=4nthc76rSl8)
			- DONE [[resource/course/Claude Code: A Highly Agentic Coding Assistant]]
			-
	- Gemini CLI
	- OpenAI Codex
	- https://www.warp.dev/coding
	- https://github.com/sst/opencode CLI opensource multi-LLM https://opencode.ai/
	-
	- Claude code addition as MCP which allows Claude to prompt other LLMs as part of its models execution https://github.com/BeehiveInnovations/zen-mcp-server
	- ~~Mulimodel CLI Crush~~ https://github.com/charmbracelet/crush
	- Kilo Code (cursor alternative but free)
	- ### Resources:
		- DONE [Terminal Agents: Codex vs. Crush vs. OpenCode vs. Cursor CLI vs. Claude Code ](https://www.youtube.com/watch?v=MXOP4WELkCc)
		- find API keys on github https://gist.github.com/win3zz/0a1c70589fcbea64dba4588b93095855
			- ```apl
			  https://github.com/search?q=%28path%3A.xml+OR+path%3A.json+OR+path%3A.properties+OR+path%3A.sql+OR+path%3A.txt+OR+path%3A.log+OR+path%3A.tmp+OR+path%3A.backup+OR+path%3A.bak+OR+path%3A.enc+OR+path%3A.yml+OR+path%3A.yaml+OR+path%3A.toml+OR+path%3A.ini+OR+path%3A.config+OR+path%3A.conf+OR+path%3A.cfg+OR+path%3A.env+OR+path%3A.env.local+OR+path%3A.env.development+OR+path%3A.env.production+OR+path%3A.env.test+OR+path%3A.envrc+OR+path%3A.prod+OR+path%3A.secret+OR+path%3A.private+OR+path%3A*.key%29+AND+%28access_key+OR+secret_key+OR+access_token+OR+api_key+OR+apikey+OR+api_secret+OR+apiSecret+OR+app_secret+OR+application_key+OR+app_key+OR+appkey+OR+auth_token+OR+authsecret%29+AND+%2Fsk-proj-%5Ba-zA-Z0-9_-%5D%7B100%2C%7D%2F&type=code&ref=advsearch
			  ```
		- DONE [ Terminal Agents: Codex vs. Crush vs. OpenCode vs. Cursor CLI vs. Claude Code ](https://www.youtube.com/watch?v=MXOP4WELkCc)
		- DONE https://www.reddit.com/r/LLMDevs/comments/1fdii62/best_llm_gateway/
		- https://github.com/Shubhamsaboo/awesome-llm-apps - prompt engineered python apps
		- TODO https://mlengineering.medium.com/mastering-llm-gateway-a-developers-guide-to-ai-model-interfacing-630bdd6216cc
		  id:: 68da381a-3012-410e-a761-4323f8c600db
		  :LOGBOOK:
		  CLOCK: [2025-09-29 Mon 10:41:19]--[2025-09-29 Mon 12:49:24] =>  02:08:05
		  :END:
		- https://www.pomerium.com/blog/best-llm-gateways-in-2025
		- DONE [How I’m Using GLM-4.5 with Claude Code (The Power Combo Set Up)](https://archive.md/8SYy5)
		- TODO https://archive.md/U8Odh
	- ### Conclusion
		- [[Sep 29th, 2025]] CLI comparison leads to Claude Code leading in all aspects. Use Claude Code cli + claude code router to allow additional models.
		- [[Sep 30th, 2025]] Use a combination of ((68db8148-7d49-4014-85ac-11bd9a813e84)) models with Claude Code cli
		- [[Oct 1st, 2025]] Hybrid approach:
			- previum tools like Claude Code (complex reasoning) + GLM (routine coding );
			- GLM (routine coding) + DeepSeek (reasoning) + GPT5 (additional routine & reasoning)
				- Cost effective compared to Claude Code
			- Combine 3 cheaper models: GLM 4.6 + Deepseek V3.1 Terminus ? + Kimi K2 Instruct 0905 ? + Qwen3 coder ???
			- use **LLM gateway** for user exposed interaction: OpenRouter or open-source self-hosted alternative;
				- Every single provider has reliability issues; using LLMs provider gateways prevents vendor lock-in and permits dynamically picking providers.
		- [[Oct 3rd, 2025]] Openrouter has a lot to offer. Ability to switch & test LLMs, protect billing from LLM provider (like chinease not allowing unsubscribe), unified billing, etc.
			-
	- # LLM keys panels:
		- https://ai.google.dev/aistudio
		- https://platform.deepseek.com/usage
		- https://platform.moonshot.ai/console
	- # Run models using docker
		- [ THIS is the REAL DEAL 🤯 for local LLMs ](https://www.youtube.com/watch?v=3XCunZqvVDA)
- ---
- # Model providers
  collapsed:: true
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
			- https://shareai.now/
	- Main coding tasks competitors: Claude Sonnet 4.5; GLM; Kimi K2; Qwen3-Coder; ChatGPT5
	  id:: 68db8148-7d49-4014-85ac-11bd9a813e84
		- Deepseek v3.1 - cheap and great for reasoning. (check providers - maybe at groq?)
		- chinease: Zhipu AI, DeepSeek, O1.AI, Moonshot AT
		- Qwen3 235B Instruct or Deepseek V3.2!
	- **Together.ai**
		- https://www.together.ai/models
	- Groq https://console.groq.com/dashboard - crazy speed
		- https://community.groq.com/
		- https://console.groq.com/docs/models
			- Model combination system offerings https://console.groq.com/docs/models#production-systems
		- https://groq.com/pricing
		- Open source app generation project based on Groq provider (official project) https://appgen.groqlabs.com/  &  https://github.com/groq/groq-appgen
	- DeepInfra (extremely cheap provider)
		- https://deepinfra.com/pricing
	- https://www.siliconflow.com/models
		- https://www.siliconflow.com/pricing
	- Qwen (Alibaba)
	- **Kimi K2**: China Moonshot https://www.moonshot.ai/
		- https://platform.moonshot.ai/docs/overview
		- https://www.kimi.com/ Kimi K2: rival to Claude Code LLMs
			- https://moonshotai.github.io/Kimi-K2/
			- Pricing https://platform.moonshot.ai/docs/pricing/chat#product-pricing
	- z.ai models https://z.ai/blog/glm-4.5
		- WARNING: issue with subscription cancellation.
		- GLM LLMs are very cheap by Zhipu AI, Beijing;
			- cons: No deep reasoning mode; web search doesn't work;  less polished output; significant privacy trade-off;
		- performance bench https://z.ai/blog/glm-4.5
		- https://docs.z.ai/scenario-example/develop-tools/claude#step-3%3A-quick-start-with-claude-code
		- https://open.bigmodel.cn/dev/api/normal-model/glm-4
	- China free models https://platform.iflow.cn/en/models
	- Gemini, Ollama, OpenAI
	- https://aihubmix.com/ chinease competitor to OpenRouter
	- [OpenRouter](https://openrouter.ai/)  (offers access to AI models - unifying endpoint)
		- **Unified Billing** and access to **Multiple LLM Providers** through a **Single API**
		- https://openrouter.ai/docs/features/provider-routing
		- https://openrouter.ai/moonshotai/kimi-k2-0905
		- [verify that this is the case] usually has a 5.5% fee on the credits purchased only; while passing through the pricing of the underlying model providers (no price change per token).
	- https://www.edenai.co/pricing
	- https://portkey.ai/pricing
	- https://www.hyperbolic.ai/inference
	- https://www.requesty.ai/solution/llm-routing/models?providers=zai
	- https://novita.ai/pricing
	- [CometAPI](https://www.cometapi.com/) (offers access to multiple AI models)
	- https://www.litellm.ai/ opensource LLM router
		- https://docs.litellm.ai/docs/
		- setup with claude code router https://docs.litellm.ai/docs/tutorials/claude_responses_api
	- https://github.com/helicone/ai-gateway/ opensource rust based
	- https://chutes.ai/
	- https://llmgateway.io/     - opensource/self-hosted LLM gateway.
	- https://github.com/katanemo/archgw
	- https://github.com/maximhq/bifrost
	- https://github.com/tensorzero/tensorzero
	- LLAMA
	- | Provider | Limits | Models | Link |
	  |----------|--------|--------|------|
	  | OpenRouter | 20 requests/min; 50 requests/day; 1000 requests/day with $10 top-up | DeepCoder 14B Preview; DeepHermes 3; DeepSeek R1; Llama 3.x; Mistral 7B; Qwen 2.5; Gemma 3; ... | https://openrouter.ai/ |
	  | Google AI Studio | 10–30 requests/min; 500–1000 requests/day (depending on model) | Gemini 1.5/2.0 Flash; Gemini 2.5; Gemma 3; LearnLM; text-embedding | https://makersuite.google.com/ |
	  | NVIDIA NIM | 40 requests/min | Various open models | https://developer.nvidia.com/nim |
	  | Mistral (La Plateforme) | 1 req/sec; 500k tokens/min; 1B tokens/month | Open/proprietary Mistral models | https://la.mistral.ai/ |
	  | Mistral (Codestral) | 30 requests/min; 2000 requests/day | Codestral | https://codestral.com/ |
	  | HuggingFace Inference Providers | $0.10/month credits | Various open models (< 10GB) | https://huggingface.co/inference-endpoints |
	  | Cerebras | 30 req/min; 60,000 tokens/min; 900 req/hr; 14,400 req/day | Qwen 3 32B; Llama 3.1/3.3/4 Scout; others | https://cerebras.net/ |
	  | Groq | model-specific; e.g., 7000 req/day & 6000 tokens/min | Allam 2 7B; DeepSeek R1 Distill 70B; Whisper Large; Gemma 2; Mistral Saba 24B; Llama 3/3.1/4; Qwen QwQ 32B; ... | https://groq.com/ |
	  | Together | 60 req/min | Llama 3.2 Vision; Llama 3.3; DeepSeek R1 Distill 70B | https://www.together.ai/ |
	  | Cohere | 20 req/min; 1000 req/month | Command-A/R/R7B/R+; Aya Expanse 8B/32B; Aya Vision 8B/32B | https://cohere.com/ |
	  | byokaashish | https://www.feedough.com/subscribe/ | www.linkedin.com/in/aashishpahwa/ | https://www.instagram.com/okaashish/ |
	- ### Claude Code LLM models providers
	  id:: 6925cb7c-2834-41f5-8c71-96eab78df80b
	  Proprietary models such as Claude Code models can be provided by thirdparty cloud providers like Amazon, Google, Azure etc.
		- Antropic AI provider (owner of Claude Code)
		- Google Vertex AI
			- provides Claude Code models
		- Amazon AWS Bedrock
			- providers Claude Code models
		- https://azure.microsoft.com/en-us/blog/introducing-claude-opus-4-5-in-microsoft-foundry/
- ---
- # LLM calculators & pricing
  collapsed:: true
	- info:
		- _many pricing comparisons are not provider comprehensive _
		- _Criteria: latency, cost, reliability, or model quality_
	- https://research.aimultiple.com/self-hosted-llm/
	- https://llmpricecheck.com/calculator/
	- TODO understand price comparison https://research.aimultiple.com/llm-pricing/
	- https://llm-price.com/
	- https://langtail.com/llm-price-comparison
	- https://yourgpt.ai/tools/openai-and-other-llm-api-pricing-calculator
	- https://llmprices.dev/#
	- https://openrouter.ai/models
		- includes provider comparison
	- https://www.helicone.ai/llm-cost
	- TODO [check if relevant] https://docs.swarms.world/en/latest/guides/pricing/
	- ==ArtificialAnalysis.ai== Providers & price comparison https://artificialanalysis.ai/leaderboards/providers
	- https://lmarena.ai/leaderboard
	- https://aider.chat/docs/leaderboards/
	- https://airank.dev/
	- https://clockbench.ai/
	-
-
- # LLM Training & Serving
	- https://inference.net/
	- LLM serving https://github.com/vllm-project/vllm  vs SGLang
	-
- # Adopting AI & tools :
	- Cursor.ai
	- local chatgpt desktop app https://github.com/Mintplex-Labs/anything-llm works with multiple LLMs.
	- https://github.com/FutureExcited/vibe-rules
	- https://rig.rs/index.html
	- ### Observability
		- Datadog
		- Grafana Cloud
		- Honeycomb
-
- ## rules:
	- TODO [https://nmn.gl/blog/cursor-guide](https://nmn.gl/blog/cursor-guide)
	- TODO [https://www.reddit.com/r/ChatGPTCoding/comments/1f4lfga/workflow_wmultiple_tools/?share_id=WgS-Ak0oJg0-cUPVyV-oG&utm_medium=ios_app&utm_name=ioscss&utm_source=share&utm_term=1](https://www.reddit.com/r/ChatGPTCoding/comments/1f4lfga/workflow_wmultiple_tools/?share_id=WgS-Ak0oJg0-cUPVyV-oG&utm_medium=ios_app&utm_name=ioscss&utm_source=share&utm_term=1)
	- TODO [Discover the Cursor Agent Workflow Nobody Is Talking About (Yet!)](https://www.youtube.com/watch?v=U_X4erx9ISY&ab_channel=FrankNillard)
	- TODO [How to make Cursor AI produce optimized SQL queries in data-access layers? | Rapid Dev](https://www.rapidevelopers.com/cursor-tutorial/how-to-make-cursor-ai-produce-optimized-sql-queries-in-data-access-layers)
		-
- ## conclusion:
	- create a submodule AI folder for AI generated services: the AI tool would touch this service specifically and make changes that encompassing repositoyry of the submodule could use without intervening with specifically written code with picked technologies.
- # Resources
	- TODO  AWS just released its Cursor killer…  https://www.youtube.com/watch?v=gA6r7iVzP6M&pp=ygUHemVkIGlkZQ%3D%3D
	- DONE [ Fun stories from building OpenRouter and where all this is going - Alex Atallah, OpenRouter ](https://www.youtube.com/watch?v=84Vtz2IL1Ug) mentions some papers worth reading
-
- # Jupyter notebook supporting Rust
	- https://github.com/evcxr/evcxr
	- https://github.com/evcxr/evcxr/blob/main/evcxr_jupyter/README.md
	- Rust Inline REPL extension vscode
	- ### Resources:
		- DONE [YT  Why I stopped using Jupyter Notebooks ](https://www.youtube.com/watch?v=qFvInA7DKuE)
		- TODO https://ratulmaharaj.com/posts/interactive-rust-with-jupyter-notebooks/
-
- --
- Voice AI:
	- https://play.ai/
- Some all in one productivity AI tool [https://skywork.ai/](https://skywork.ai/)
- ---
- # Resources Articles & news:
	- https://medium.com/data-science-collectived
	- https://news.smol.ai/
	-