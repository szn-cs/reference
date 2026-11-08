## [[Tech Stack/AI, LLMs, Agents/Agent Framework]] ^tech-stack-ai-llms-agents-agent-framework
## [[Tech Stack/AI, LLMs, Agents/LLMs]]
## [[Tech Stack/AI, LLMs, Agents/Model providers & inference]]
# Coding agents tools & models: ^coding-agents-tools-models

- info:
    - _24 million tokens/day** —enough for 3–4 hours of uninterrupted vibe coding_ while _heavy coding workflows up to 120m tokens/day_
- List of coding agents
    - [https://models.dev/](https://models.dev/)
    - [https://huggingface.co/models](https://huggingface.co/models)
    - [https://models.litellm.ai/](https://models.litellm.ai/)
    - performance analysis [https://artificialanalysis.ai/](https://artificialanalysis.ai/)
        - [https://artificialanalysis.ai/leaderboards/models](https://artificialanalysis.ai/leaderboards/models)
        - [https://artificialanalysis.ai/models/capabilities/coding](https://artificialanalysis.ai/models/capabilities/coding)
        - [https://onyx.app/llm-leaderboard](https://onyx.app/llm-leaderboard)
        - [https://zenmux.ai/models?sort=newest](https://zenmux.ai/models?sort=newest)
    - provides a good list of models and fileters [https://deepinfra.com/models](https://deepinfra.com/models)
    - [https://github.com/cheahjs/free-llm-api-resources](https://github.com/cheahjs/free-llm-api-resources)
    - China opensource community [https://modelscope.cn/home](https://modelscope.cn/home)
    - OpenAI Custom GPTs
    - Bolt.new
    - Dust.tt - internal AI tools/workflows
    - Flowise - LLM workflows on LangChain
    - Cognosys - autonomous-agent for planning and execution.
    - Bubble - web apps
    - Glide data-driven apps (spreadsheet/databases).
- **Claude Code** ^claude-code
    - info :
        - accessible through CLI or SDK.
            - thirdparty providers may allow free tier access (Google, Amazon bedrock)
        - specific phrases are mapped directly to increasing levels of thinking budget in the system: "think" < "think hard" < "think harder" < "ultrathink." Each level allocates progressively more thinking budget for Claude to use.
        - subagents allow to modify the system prompt but has shortcoming in other places.
        - Claude API pay-as-you-go is a lot more expensive  than the subscription-based when used heavily.
        - Claude code is able ot keep track of progress in working scratchpad.
        - Claude Code supports headless mode for non-interactive contexts
    - [https://claude.com/product/claude-code](https://claude.com/product/claude-code)
    - Prompt improver [https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/prompt-improver](https://platform.claude.com/docs/en/build-with-claude/prompt-engineering/prompt-improver)
    - [https://docs.claude.com/en/api/agent-sdk/overview](https://docs.claude.com/en/api/agent-sdk/overview) - Python + TS/JS
    - [https://claudelog.com/](https://claudelog.com/)
    - SDK agents [https://docs.claude.com/en/docs/claude-code/sdk/sdk-overview](https://docs.claude.com/en/docs/claude-code/sdk/sdk-overview)
        - API interface to access Claude Code grammatically.
        - Some tools follow the Agent Client Protocal [https://agentclientprotocol.com/overview/introduction](https://agentclientprotocol.com/overview/introduction)
    - Intercepts Claude Code cli communication and redirects to different LLMs [https://github.com/musistudio/claude-code-router](https://github.com/musistudio/claude-code-router)
        - docs [https://github.com/musistudio/claude-code-router](https://github.com/musistudio/claude-code-router)
        - config example [https://github.com/musistudio/claude-code-router/blob/main/ui/config.example.json](https://github.com/musistudio/claude-code-router/blob/main/ui/config.example.json)
        - blog articles [https://github.com/musistudio/claude-code-router/tree/main/blog/en](https://github.com/musistudio/claude-code-router/tree/main/blog/en)
        - cheatsheet [https://awesomeclaude.ai/code-cheatsheet](https://awesomeclaude.ai/code-cheatsheet)
    - > "Run multiple Task invocations in a SINGLE message" to run in parallel
    - [ ] [https://github.com/ericbuess/claude-code-docs](https://github.com/ericbuess/claude-code-docs)
    - [ ] [https://github.com/zilliztech/claude-context](https://github.com/zilliztech/claude-context)   or   [https://github.com/ericbuess/claude-code-project-index](https://github.com/ericbuess/claude-code-project-index)
    - [ ] checkout tool MCP task management system for [https://github.com/eyaltoledano/claude-task-master](https://github.com/eyaltoledano/claude-task-master)
    - **Plugins marketplace** ^plugins-marketplace
        - [https://github.com/wshobson/agents](https://github.com/wshobson/agents)
        - [https://claudecodemarketplace.com/](https://claudecodemarketplace.com/)
        - [https://github.com/edmund-io/edmunds-claude-code](https://github.com/edmund-io/edmunds-claude-code) from [youtuber](https://www.youtube.com/watch?v=Ffh9OeJ7yxw)
    - **MCP :**
        - [https://context7.com/](https://context7.com/)
            - [x] install [https://github.com/upstash/context7](https://github.com/upstash/context7)
        - [x] [https://github.com/21st-dev/magic-mcp](https://github.com/21st-dev/magic-mcp) create magic interfaces
        - [ ] [https://ref.tools/](https://ref.tools/) ^https-ref-tools
        - [ ] [https://exa.ai/](https://exa.ai/) ^https-exa-ai
        - [x] [https://docs.firecrawl.dev/mcp-server](https://docs.firecrawl.dev/mcp-server) mcp ^https-docs-firecrawl-dev-mcp-server-mcp
        - [ ] [https://help.figma.com/hc/en-us/articles/32132100833559-Guide-to-the-Figma-MCP-server](https://help.figma.com/hc/en-us/articles/32132100833559-Guide-to-the-Figma-MCP-server)
        - **Vercel AI SDK** in npm acts as a client implementation for MCP servers. [https://vercel.com/blog/ai-sdk-4-2](https://vercel.com/blog/ai-sdk-4-2)
        - Vercel's access browser agent CLI tool for claude code and similar agents [https://github.com/vercel-labs/agent-browser](https://github.com/vercel-labs/agent-browser)
        - [https://browser-use.com/](https://browser-use.com/)
        - **supergateway** adaptation layer wraps a stdio MCP server and exposes it over HTTP using Server-Sent Events (SSE). Translates communication from stdio ↔ HTTP/SSE
        - **Resources**
            - [https://mcpservers.org/](https://mcpservers.org/)
            - [https://github.com/modelcontextprotocol/servers](https://github.com/modelcontextprotocol/servers)
            - [https://mcpmarket.com/](https://mcpmarket.com/)
            - [https://github.com/modelcontextprotocol/servers](https://github.com/modelcontextprotocol/servers) list
            - MCP containerized servers [https://hub.docker.com/mcp](https://hub.docker.com/mcp)
    - **Skills & Prompts**
        - official calude code skills [https://github.com/anthropics/claude-code/tree/main/plugins/frontend-design](https://github.com/anthropics/claude-code/tree/main/plugins/frontend-design)
        - **System prompts of claude** - Claude's production prompt architecture extracted prompts completely as in production as of Dec 15 2025 [https://github.com/Piebald-AI/claude-code-system-prompts](https://github.com/Piebald-AI/claude-code-system-prompts) ^system-prompts-of-claude-claude-s-production-prompt
        - system prompts of many agents [https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools/tree/main](https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools/tree/main)
        - [https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools/](https://github.com/x1xhlol/system-prompts-and-models-of-ai-tools/)
        - [https://github.com/anthropics/claude-code/tree/main/plugins](https://github.com/anthropics/claude-code/tree/main/plugins) combines subagents, mcps, & skills.
        - subagents examples [https://github.com/lst97/claude-code-sub-agents/tree/main/agents](https://github.com/lst97/claude-code-sub-agents/tree/main/agents) examples (from [youtuber](https://www.youtube.com/@edmundyong))
    - **Resources**
        - [ ] [I should be charging $999 for this Claude Code Tutorial](https://www.youtube.com/watch?v=4nthc76rSl8)
        - [x] [[resource/course/Claude Code: A Highly Agentic Coding Assistant]]
    - Gemini CLI
    - OpenAI Codex
    - [https://www.warp.dev/coding](https://www.warp.dev/coding)
    - [https://github.com/sst/opencode](https://github.com/sst/opencode) CLI opensource multi-LLM [https://opencode.ai/](https://opencode.ai/)
    - xAI
    - Meta Llama
    - LlamaIndex
    - Mistral
    - Mistra
    - Claude code addition as MCP which allows Claude to prompt other LLMs as part of its models execution [https://github.com/BeehiveInnovations/zen-mcp-server](https://github.com/BeehiveInnovations/zen-mcp-server)
    - ~~Mulimodel CLI Crush~~ [https://github.com/charmbracelet/crush](https://github.com/charmbracelet/crush)
    - Kilo Code (cursor alternative but free)
    - **codegen tools:**
        - [Morph](https://www.morphllm.com/) or [Relace](https://www.relace.ai/) tools which are used in codegen AI for fast edits (using diffs instead of entire rewrite) and adds a layer of code/syntax validation.
            - code replacement and AST modification tools - wrap LLMs like Claude to apply code edits more safely or ergonomically.
                - Note: it seems Claude Agent SDK already has sufficient diff-based editting instead of entire file edits.
            - > **MorphLLM and Relace.ai are commercial platforms that implement generative code editing workflows — MorphLLM focusing on structural AST transformations and Relace.ai on replacement/diff-centric generation — while the underlying strategies they use can be (and often are) built with open-source tooling as well.
    - **Resources:**
        - [x] [Terminal Agents: Codex vs. Crush vs. OpenCode vs. Cursor CLI vs. Claude Code ](https://www.youtube.com/watch?v=MXOP4WELkCc)
        - find API keys on github [https://gist.github.com/win3zz/0a1c70589fcbea64dba4588b93095855](https://gist.github.com/win3zz/0a1c70589fcbea64dba4588b93095855)
            - ```apl
              https://github.com/search?q=%28path%3A.xml+OR+path%3A.json+OR+path%3A.properties+OR+path%3A.sql+OR+path%3A.txt+OR+path%3A.log+OR+path%3A.tmp+OR+path%3A.backup+OR+path%3A.bak+OR+path%3A.enc+OR+path%3A.yml+OR+path%3A.yaml+OR+path%3A.toml+OR+path%3A.ini+OR+path%3A.config+OR+path%3A.conf+OR+path%3A.cfg+OR+path%3A.env+OR+path%3A.env.local+OR+path%3A.env.development+OR+path%3A.env.production+OR+path%3A.env.test+OR+path%3A.envrc+OR+path%3A.prod+OR+path%3A.secret+OR+path%3A.private+OR+path%3A*.key%29+AND+%28access_key+OR+secret_key+OR+access_token+OR+api_key+OR+apikey+OR+api_secret+OR+apiSecret+OR+app_secret+OR+application_key+OR+app_key+OR+appkey+OR+auth_token+OR+authsecret%29+AND+%2Fsk-proj-%5Ba-zA-Z0-9_-%5D%7B100%2C%7D%2F&type=code&ref=advsearch
              ```
        - [x] [ Terminal Agents: Codex vs. Crush vs. OpenCode vs. Cursor CLI vs. Claude Code ](https://www.youtube.com/watch?v=MXOP4WELkCc)
        - [x] [https://www.reddit.com/r/LLMDevs/comments/1fdii62/best_llm_gateway/](https://www.reddit.com/r/LLMDevs/comments/1fdii62/best_llm_gateway/)
        - [https://github.com/Shubhamsaboo/awesome-llm-apps](https://github.com/Shubhamsaboo/awesome-llm-apps) - prompt engineered python apps
        - [ ] [https://mlengineering.medium.com/mastering-llm-gateway-a-developers-guide-to-ai-model-interfacing-630bdd6216cc](https://mlengineering.medium.com/mastering-llm-gateway-a-developers-guide-to-ai-model-interfacing-630bdd6216cc) ^https-mlengineering-medium-com-mastering-llm-gateway-a
        - [https://www.pomerium.com/blog/best-llm-gateways-in-2025](https://www.pomerium.com/blog/best-llm-gateways-in-2025)
        - [x] [How I’m Using GLM-4.5 with Claude Code (The Power Combo Set Up)](https://archive.md/8SYy5)
        - [ ] [https://archive.md/U8Odh](https://archive.md/U8Odh)
    - **Conclusion**
        - [[2025-09-29]] CLI comparison leads to Claude Code leading in all aspects. Use Claude Code cli + claude code router to allow additional models.
        - [[2025-09-30]] Use a combination of [[Tech Stack/AI, LLMs, Agents/Model Providers/journal#^main-coding-tasks-competitors-claude-sonnet-4-5-glm-kimi-k2]] models with Claude Code cli
        - [[2025-10-01]] Hybrid approach:
            - previum tools like Claude Code (complex reasoning) + GLM (routine coding );
            - GLM (routine coding) + DeepSeek (reasoning) + GPT5 (additional routine & reasoning)
                - Cost effective compared to Claude Code
            - Combine 3 cheaper models: GLM 4.6 + Deepseek V3.1 Terminus ? + Kimi K2 Instruct 0905 ? + Qwen3 coder ???
            - use **LLM gateway** for user exposed interaction: OpenRouter or open-source self-hosted alternative;
                - Every single provider has reliability issues; using LLMs provider gateways prevents vendor lock-in and permits dynamically picking providers.
        - [[2025-10-03]] Openrouter has a lot to offer. Ability to switch & test LLMs, protect billing from LLM provider (like chinease not allowing unsubscribe), unified billing, etc.
    - **LLM keys panels:**
        - [https://ai.google.dev/aistudio](https://ai.google.dev/aistudio)
        - [https://platform.deepseek.com/usage](https://platform.deepseek.com/usage)
        - [https://platform.moonshot.ai/console](https://platform.moonshot.ai/console)
    - **Run models using docker**
        - [ THIS is the REAL DEAL 🤯 for local LLMs ](https://www.youtube.com/watch?v=3XCunZqvVDA)

---
# LLM calculators & pricing

- info:
    - _many pricing comparisons are not provider comprehensive _
    - _Criteria: latency, cost, reliability, or model quality_
- [https://research.aimultiple.com/self-hosted-llm/](https://research.aimultiple.com/self-hosted-llm/)
- [https://llmpricecheck.com/calculator/](https://llmpricecheck.com/calculator/)
- [ ] understand price comparison [https://research.aimultiple.com/llm-pricing/](https://research.aimultiple.com/llm-pricing/)
- [https://llm-price.com/](https://llm-price.com/)
- [https://langtail.com/llm-price-comparison](https://langtail.com/llm-price-comparison)
- [https://yourgpt.ai/tools/openai-and-other-llm-api-pricing-calculator](https://yourgpt.ai/tools/openai-and-other-llm-api-pricing-calculator)
- [https://llmprices.dev/#](https://llmprices.dev/#)
- [https://openrouter.ai/models](https://openrouter.ai/models)
    - includes provider comparison
- [https://www.helicone.ai/llm-cost](https://www.helicone.ai/llm-cost)
- [ ] [check if relevant] [https://docs.swarms.world/en/latest/guides/pricing/](https://docs.swarms.world/en/latest/guides/pricing/)
- ==ArtificialAnalysis.ai== Providers & price comparison [https://artificialanalysis.ai/leaderboards/providers](https://artificialanalysis.ai/leaderboards/providers)
- [https://lmarena.ai/leaderboard](https://lmarena.ai/leaderboard)
- [https://aider.chat/docs/leaderboards/](https://aider.chat/docs/leaderboards/)
- [https://airank.dev/](https://airank.dev/)
- [https://clockbench.ai/](https://clockbench.ai/)
- [https://arena.ai/leaderboard/code/webdev](https://arena.ai/leaderboard/code/webdev)
- Models by usage popularity [https://openrouter.ai/rankings#top-models](https://openrouter.ai/rankings#top-models)

# LLM Training & Serving

- [https://inference.net/](https://inference.net/)
- LLM serving [https://github.com/vllm-project/vllm](https://github.com/vllm-project/vllm)  vs SGLang

# Adopting AI & tools :

- Cursor.ai
- local chatgpt desktop app [https://github.com/Mintplex-Labs/anything-llm](https://github.com/Mintplex-Labs/anything-llm) works with multiple LLMs.
- [https://github.com/FutureExcited/vibe-rules](https://github.com/FutureExcited/vibe-rules)
- [https://rig.rs/index.html](https://rig.rs/index.html)
- **Observability**
    - Datadog
    - Grafana Cloud
    - Honeycomb
    - [https://pydantic.dev/logfire](https://pydantic.dev/logfire) - what base44 uses.
- [https://agents.md/](https://agents.md/) open standard for coding agent instructions.
- Search for AI tool [https://parallel.ai/](https://parallel.ai/)
- **agent orchestration tools**
    - agents to run business [https://github.com/paperclipai/paperclip](https://github.com/paperclipai/paperclip)
    - Claw-Empire

## rules:

- [ ] [https://nmn.gl/blog/cursor-guide](https://nmn.gl/blog/cursor-guide)
- [ ] [https://www.reddit.com/r/ChatGPTCoding/comments/1f4lfga/workflow_wmultiple_tools/?share_id=WgS-Ak0oJg0-cUPVyV-oG&utm_medium=ios_app&utm_name=ioscss&utm_source=share&utm_term=1](https://www.reddit.com/r/ChatGPTCoding/comments/1f4lfga/workflow_wmultiple_tools/?share_id=WgS-Ak0oJg0-cUPVyV-oG&utm_medium=ios_app&utm_name=ioscss&utm_source=share&utm_term=1)
- [ ] [Discover the Cursor Agent Workflow Nobody Is Talking About (Yet!)](https://www.youtube.com/watch?v=U_X4erx9ISY&ab_channel=FrankNillard)
- [ ] [How to make Cursor AI produce optimized SQL queries in data-access layers? | Rapid Dev](https://www.rapidevelopers.com/cursor-tutorial/how-to-make-cursor-ai-produce-optimized-sql-queries-in-data-access-layers)

## conclusion:

- create a submodule AI folder for AI generated services: the AI tool would touch this service specifically and make changes that encompassing repositoyry of the submodule could use without intervening with specifically written code with picked technologies.

# Resources

- [ ] AWS just released its Cursor killer…  [https://www.youtube.com/watch?v=gA6r7iVzP6M&pp=ygUHemVkIGlkZQ%3D%3D](https://www.youtube.com/watch?v=gA6r7iVzP6M&pp=ygUHemVkIGlkZQ%3D%3D)
- [x] [ Fun stories from building OpenRouter and where all this is going - Alex Atallah, OpenRouter ](https://www.youtube.com/watch?v=84Vtz2IL1Ug) mentions some papers worth reading

# Jupyter notebook supporting Rust

- [https://github.com/evcxr/evcxr](https://github.com/evcxr/evcxr)
- [https://github.com/evcxr/evcxr/blob/main/evcxr_jupyter/README.md](https://github.com/evcxr/evcxr/blob/main/evcxr_jupyter/README.md)
- Rust Inline REPL extension vscode
- **Resources:**
    - [x] [YT  Why I stopped using Jupyter Notebooks ](https://www.youtube.com/watch?v=qFvInA7DKuE)
    - [ ] [https://ratulmaharaj.com/posts/interactive-rust-with-jupyter-notebooks/](https://ratulmaharaj.com/posts/interactive-rust-with-jupyter-notebooks/)
- --
- Voice AI:
    - [https://play.ai/](https://play.ai/)
- Some all in one productivity AI tool [https://skywork.ai/](https://skywork.ai/)

---
# Resources Articles & news:

- [https://medium.com/data-science-collectived](https://medium.com/data-science-collectived)
- [https://news.smol.ai/](https://news.smol.ai/)
