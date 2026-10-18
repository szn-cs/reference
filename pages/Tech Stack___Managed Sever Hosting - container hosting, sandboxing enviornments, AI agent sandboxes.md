# Third-party managed services for hosting servers and isolation environment providers for user projects
id:: 691e7a6b-c3c7-4878-b986-db185539694a
collapsed:: true
	- #+BEGIN_QUOTE
	  - **E2B**
	    - DESC: Ephemeral execution + sandboxed integrations (ideal for code/agent workflows)
	    - HIGHLIGHTS:
	      - Rapid sandbox startup, tool-integration SDKs
	      - Focus on agent/code execution in isolated environments
	    - CONSIDERATIONS: Niche; check maturity for production governance
	  
	  - **Daytona**
	    - DESC: Sandboxes for code execution with full lifecycle management
	    - HIGHLIGHTS:
	      - Strong isolation, configurable resources
	      - API/SDK for sandbox create/stop/delete
	    - CONSIDERATIONS: Requires more infrastructure control; cost varies with resources
	  
	  - **Signadot**
	    - DESC: Lightweight ephemeral environments for microservices per pull request
	    - HIGHLIGHTS:
	      - Efficient preview/test environments for microservices stacks
	    - CONSIDERATIONS: Less suited for heavy agent/tool-integration workflows
	  
	  - **Uffizzi**
	    - DESC: Kubernetes-based ephemeral preview environments triggered by PRs
	    - HIGHLIGHTS:
	      - Full-stack previews (services + DBs)
	      - GitHub/GitLab integration
	    - CONSIDERATIONS: Requires K8s knowledge and infrastructure
	  
	  - **Humanitec**
	    - DESC: Internal Developer Platform with on-demand ephemeral environments
	    - HIGHLIGHTS:
	      - Dev self-service
	      - Platform engineering-driven provisioning
	    - CONSIDERATIONS: Heavy for lean teams; adds platform engineering overhead
	  
	  - **Bunnyshell**
	    - DESC: Environment-as-a-Service offering ephemeral environments per PR
	    - HIGHLIGHTS:
	      - YAML templates
	      - Full-stack previews, cost control
	    - CONSIDERATIONS: Good for dev/test; limited agent/tool integration
	  
	  - **Render**
	    - DESC: On-demand staging / ephemeral environments for web/apps
	    - HIGHLIGHTS:
	      - Quick deployment, simple UI
	      - Suitable for full-stack apps and front-end previews
	    - CONSIDERATIONS: Less granular sandbox isolation
	  
	  - **Vercel**
	    - DESC: Automated preview environments for frontend/serverless stacks
	    - HIGHLIGHTS:
	      - Fast iteration, tight Next.js integration
	    - CONSIDERATIONS: Limited for backend or DB-heavy workflows
	  
	  - **Qovery**
	    - DESC: Full-stack ephemeral environments / PaaS-style previews triggered by PRs
	    - HIGHLIGHTS:
	      - Deploys full applications across services/containers
	      - Integrates with Git workflows
	    - CONSIDERATIONS: More setup than frontend-only preview tools
	  
	  - **Shipyard**
	    - DESC: Ephemeral environments for dev/test/agent workflows
	    - HIGHLIGHTS:
	      - Quick review cycles for QA/product teams
	    - CONSIDERATIONS: Less control than full sandbox platforms
	  
	  - **Porter**
	    - DESC: Kubernetes platform with preview deploys + secrets + one-click environments
	    - HIGHLIGHTS:
	      - Helm support, branch-based previews
	    - CONSIDERATIONS: Requires K8s infrastructure; learning curve
	  
	  - **Okteto**
	    - DESC: Kubernetes developer environments with auto-previews
	    - HIGHLIGHTS:
	      - Namespace isolation per branch
	      - Streamlined dev experience
	    - CONSIDERATIONS: Best for self-hosted K8s; managed options limited
	  
	  - **Netlify**
	    - DESC: JAMstack deploy previews / branch previews for static sites
	    - HIGHLIGHTS:
	      - Excellent for frontend feedback loops
	    - CONSIDERATIONS: Not built for backend or agent sandboxing
	  
	  - **Encore (Cloud Pro)**
	    - DESC: Preview environments for each PR in managed form
	    - HIGHLIGHTS:
	      - Integrated DB branching (Neon)
	      - Full backend+frontend support
	    - CONSIDERATIONS: Newer product; validate scale limits
	  
	  - **Octopus Deploy (Ephemeral Environments)**
	    - DESC: Adds ephemeral environment capability for branch deployments
	    - HIGHLIGHTS:
	      - Integrates with deployment pipelines
	      - Auto-deprovisioning after inactivity
	    - CONSIDERATIONS: Focused on deployment/testing, not tool sandboxes
	  #+END_QUOTE
	-
- ---
- # Third-party managed container hosting
  id:: 691e7abd-2e8b-40bc-8c1c-d60324bde359
  collapsed:: true
	- Comprehensive Single Container Hosting Options with Details
		- Criteria sought for: price, features, compatibility DB, intended scale, managed containers service
			- #+BEGIN_QUOTE
			  - Criteria / Trade-offs
			  	- **Ops complexity:** PaaS < VPS < Orchestration
			  	- **Cost:** PaaS pay-per-use is best for early stage
			  	- **Integration:** ==Confirm DB or service compatibility== (Postgres, MySQL, Redis, etc.)
			  	- **Performance:** Edge or regional providers (Fly.io, Scaleway, Hetzner) can reduce latency
			  	- **Scaling path:** Kubernetes platforms support horizontal scaling and autoscaling
			  	- **Lock-in:** Proprietary PaaS platforms trade flexibility for speed
			  	- **Security:** Check compliance (SOC2, GDPR, HIPAA) for production workloads
			  	- **Monitoring:** Ensure metrics, logs, and tracing support (Prometheus, Grafana, CloudWatch)
			  	- **Cost visibility:** Always factor in storage and outbound network traffic
			  #+END_QUOTE
	-
	- _Managed / PaaS Runtimes (minimal ops)_
		- **Google Cloud Run**
		- **AWS App Runner**
		- **Azure Container Apps**
		- **Azure Container Instances (ACI)**
		- **Render**
			- cheaper, flat pricing.
		- **Fly.io**
			- "infrastructure ownership: Fly.io runs your applications on their own hardware and infrastructure"
			- autoscales vertically & horizontally; supports suspension (stops instance when not in use).
		- **Railway.app**
			- expensive
		- **Platform.sh**
			- expensive
		- **Jelastic (Virtuozzo Application Platform)**
		- **Hyve Managed Containers**
	- _Lightweight Platforms / VPS_
		- **DigitalOcean App Platform**
		- **Heroku (Docker Deploy)**
		- https://dokku.com/ open source Heroku alternative
		- **Cycle.io**
		- **Linode Kubernetes Engine (LKE)**
		- **Vultr Kubernetes Engine (VKE)**
		- **OVHcloud Managed Kubernetes**
		- **Scaleway Kubernetes Kapsule**
		- **Hetzner Cloud + Docker**
		- **Kamatera**
		- **Portainer Cloud**
		- **Mirantis Kubernetes Engine (formerly Docker Enterprise)**
		- **Rancher**
		- **Scalr**
	- _Orchestration / Full Control_
		- **AWS ECS + Fargate**
		- **AWS EKS**
		- **Google Kubernetes Engine (GKE)**
		- **Azure Kubernetes Service (AKS)**
		- **Red Hat OpenShift**
		- **Linode LKE**
		- **Vultr VKE**
		- **Scaleway Kapsule**
		- **OVHcloud K8s**
	- _others_
		- Hostinger
		- SiteGround
		- Verpex
		- [https://kuberns.com/](vhttps://www.reddit.com/r/elixir/comments/1h8ajm2/is_flyio_ridiculously_expensive/.com/)
		- TODO check for offshore providers
		-
	- ### comparison
		- #+BEGIN_QUOTE
		  	- **For simplicity:** *Google Cloud Run*, *Render*, or *Railway.app*
		  	- **For portability:** *Fly.io*, *Hetzner Cloud*, or *Cycle.io*
		  	- **For global latency:** *Fly.io* or *Scaleway Kapsule*
		  	- **For scaling and enterprise features:** *AWS ECS (Fargate)*, *GKE*, *AKS*, or *OpenShift*
		  	- **For budget-conscious teams:** *Linode LKE*, *Vultr VKE*, or *OVHcloud Managed Kubernetes*
		  #+END_QUOTE
		- #+BEGIN_QUOTE
		  - **Hetzner VPS**
		    - Type: VPS, self-managed  
		    - Starting Cost: ~$4/month  
		    - Best for: Full control, persistent, cost-effective  
		  
		  - **Google Cloud Run**
		    - Type: Serverless Containers  
		    - Starting Cost: Free tier + pay usage  
		    - Best for: Event-driven, variable workloads  
		  
		  - **Fly.io**
		    - Type: Edge Serverless, VPC VMs
		    - Starting Cost: Free tier + ~$1.94/month  
		    - Best for: Global low latency, hobby projects  
		  
		  - **Shuttle.rs**
		    - Type: Rust-specific PaaS  
		    - Starting Cost: Free tier + paid plans  
		    - Best for: Rapid Rust app prototyping  
		  
		  - **Railway.app**
		    - Type: PaaS  
		    - Starting Cost: $5 credit initial  
		    - Best for: Backend APIs, databases               
		  #+END_QUOTE
		- #+BEGIN_QUOTE
		  if the goal is minimal operations with managed containers and the cheapest price, the order to consider is:
		  Fly.io < DigitalOcean App Platform < Render < Google Cloud Run < AWS App Runner < Azure Container Apps
		  #+END_QUOTE
	- # Conclusion
	  for simple fast low-cost apps
		- The issue with Render, Railway.app services
			- cost adds up for simple services (e.g. managed databse another 7$, ....);
			- cold-start latency issue for lower-tiers?
		- ==Fly.io==
			- has more predictable pricing (seems to be cheaper than render);
			- platform founder is transparent about cloud situation, improvement approaches and future direction.
			- issue: does not collocation database with app region.
			- proprietary container on firecracker VMs + managed kubernetes k3s + Postgresql
			- Future/vision of Fly.io is to allow LLM agent behavior and stateful apps usecases to run seamlessly with low startup time.
				- Fly.io wants to give users UX (like vercel) + great infrastructure primitives/solutions (like Amazon) for the age of AI.
			- Charges around 100% markup on its VMs (machines).
- ---
- # AI agent sandbox multitenant infrastructure providers for no-code platformhttps://e2b.dev/
  id:: 691e7a58-3222-45a0-aca0-c74e5fae627d
	- characteristics: ephemeral, instant startup, long term storage, scalable, secure/isolated execution environments for executing untrusted user code or for executing AI agent logic;
		- AI agents compared to standard software should not be assumed predictable application behavior, rather the agents execute arbitrary untrusted code generated during runtime.
		- > Agents Hosting is a serverless computing service that allows you to host any application without having to manage infrastructure. It gives you full observability and tracing out of the box.
		- Sandboxing technology:
			- **Containers** — Shared kernel; trusting the **host kernel**
			- **gVisor** — Userspace kernel; trusting the **userspace kernel implementation**
			- Kata container is VM-backed container runtime - supporting firecracker VM, by default QEMU.
			- **MicroVMs** — Guest kernel; trusting the **guest kernel + VMM**
			- **Wasm** — No syscall ABI; trusting the **runtime + embedder**
			- OS-provided sandboxing tools - Landlock Linux (Searbelt Apple, AppContainer Windows): allow to limit process access, used to protect user data on local machine.
			- ~~Rust-based https://github.com/abshkbh/arrakis~~
		- #+BEGIN_QUOTE
		  general features of sandbox provider:
		  - Real-time Terminal: Persistent terminals via WebSockets for interactive work.
		  - Filesystem Control: Full file CRUD with real-time change notifications.
		  - Programmatic API: TypeScript-first API with strong error handling.
		  - Process Control: Start, stop, and monitor processes with port forwarding.
		  - JWT Security: Token-based authentication for secure execution.
		  - One-off Commands: Run commands instantly without persistent sessions.
		  - WebSocket I/O: Low-latency, bidirectional communication.
		  - Health Checks: Built-in status and health monitoring.
		  #+END_QUOTE
	- ==[E2B](https://e2b.dev/)==: disposable cloud sandboxes Firecracker (OSS) microVM sandboxes that can run servers, execute terminal commands, and expose ports publicly with hosted SaaS (use E2B's managed cloud) or bring-your-own-cloud. Characterized by openness and management flexibility; https://github.com/e2b-dev/E2B
		- SDKs: Python, JavaScript, Bash, R, Java; open-source with SDKs for flexible microVM sandbox control.
		- Pioneered the concept of running AI generated code in a secure isolated sandbox;
		- https://lobehub.com/mcp/griffinwork40-e2b-mcp MCP to the E2B SDK
	- **[Runloop](https://www.runloop.ai/)**
	- **Modal** Sandboxes: gVisor-based, Python SDK orchestration, high concurrency. for advanced/ML-heavy workloads; Container-based sandbox with GPU support, fast startup, serverless ML compute.
	- https://sprites.dev/ ephemeral sandboxing with persistence by Fly.io
	- other legacy considerations: AWS Firecracker, gVisor stacks
	- **Daytona**: fast Docker/Kata sandboxes
		- SDK: Python SDK, Docker-based devcontainer support; focused on fast container startup for AI workloads.
		- Secure container sandboxes, very low latency, early-stage agent runtime. Runs on kubernetes
	- App hosting PaaS
		- **Render**: ~sandboxes for serverless containerized applications~; app hosting, this is more of a cloud for containerized applications
			- Awesome PaaS with full featured for production ready applications.
			- Lack of scaling and lack of granular control for sandboxes;
			- SDK: REST/CLI interfaces; no advanced sandbox-specific SDKs, focuses on app hosting.
		- https://kuberns.com/about - app hosting
		- > Heroku    Render     Railway     NorthFlank    Fly   DigitalOcean
		- **Fly.io** Firecracker microVMs, limited sandbox orchestration;
			- Not primarily built for AI agent sandboxing: Many no-code platforms at early stage used it but migrated away due to scalability issues.
			- SDKs: CLI and REST API primarily; limited native SDKs for programming languages.
	- Northflank: Alternative to Modal with support for any OCI container, robust multi-tenant isolation; gaining traction for production-scale agent work; MicroVM/container platform, enterprise isolation;
	- **Cloudflare Workers/Vercel Sandbox:** Isolated JavaScript/V8 sandboxes—less common for untrusted AI, more for edge-only or webhook-type code; Edge execution, low latency, JS/WASM agent code.
	- Google Agent Sandbox https://cloud.google.com/blog/products/containers-kubernetes/agentic-ai-on-kubernetes-and-gke
		- https://github.com/kubernetes-sigs/agent-sandbox
	- DifySandbox
	- CodeSandbox SDK
	- **Blaxel** – MicroVM sandbox, ultra-fast resume, scale-to-zero, SDKs for agents. perpetual sandbox platform (maintain state indefinitely).
	- **Together Code Sandbox** – Persistent VM-style environments, heavy workloads, versioned storage.
	- **Novita AI Sandbox** – Fast, isolated execution for parallel agent tasks.
	- **Koyeb Sandboxes** – Ephemeral containers, scalable execution, general AI code sandboxing.
	- **Docker + MCP Gateway** – Containerized workloads, integrates with orchestration.
	- alternative/complimentary approach as used by some AI no-code platforms which rely on mixed approach and provide additional options to users:
		- running lighter-weight workflows in browser via WebAssembly-based WebContainers.
		- Proprietary Replit Sandboxed Runtime (hybrid Firecracker/gVisor environments, orchestration closely coupled to their cloud platform - Google Cloud, built for massive scale).
		- customer-dedicated Firecracker on AWS.
		- custom container (Docker/Kubernetes) orchestration; isolation enhanced with gVisor, Kata Containers, or Firecracker microVMs;
		- **Microsandbox Rust SDK** https://github.com/zerocore-ai/microsandbox Rust-based project
			- #+BEGIN_QUOTE
			  - Function & Key Features: Secure Execution. A **Rust-based SDK** for spinning up lightweight, secure **microVMs**. Designed for **high-performance isolation**, running untrusted AI-generated code in milliseconds.
			  - **Stack**: **Rust-based SDK** (crates.io), provides interfaces for executing **Python** and **JavaScript** code, powered by the **libkrun** virtualization library.
			  #+END_QUOTE
		- reference implementation to minimize risks when using claude --dangerously-skip-permissions https://github.com/anthropics/claude-code/tree/main/.devcontainer in a container without internet access.
		- other tailored for coding environments CDEs like Gitpod and Coder.
	- https://runloop.ai/ simpler platforms achieving AI agent sandboxing
	- AWS Bedrock AgentCore
	- [#C] Beam
	- Microsandbox opensource alternative to E2B sandboxing solution (no rich features)
	- DifySandbox opensource sandboxing solution (no rich features)
	- https://vercel.com/solutions/multi-tenant-saas
	- https://railway.com/
	- CodeSandbox
	- ComputeSDK sandboxing: Multi-provider Support: Use E2B, Vercel, Daytona, Modal, CodeSandbox, and others with no code changes.
	- ### Adoption by AI-native platforms
		- > | Platform     | Current Provider(s)    | Previously Used            |
		  | ------------ | ---------------------- | -------------------------- |
		  | Lovable.dev  | Modal                  | Fly.io, legacy AWS/K8s     |
		  | Bolt.new     | E2B, Modal, Daytona    | Fly.io (early), Vercel     |
		  | Base44       | E2B, AWS Firecracker   | Render, Modal (pilot)      |
		  | Replit       | Replit (proprietary)   | N/A                        |
		  | WeWeb/Bubble | Kubernetes/gVisor/Kata | AWS Lambda, Vercel, Render |
		  | Daytona/E2B  | Self-host, SaaS        | Not applicable (new)       |
		- #+BEGIN_QUOTE
		  - **Lovable.dev**: Uses Modal AI SDK for AI sandbox and orchestration.
		  - **Bolt.new**: Uses E2B SDK with Kubernetes orchestration for sandboxes.
		  - **Base44**: Primarily leverages E2B SDK for coding sandboxes.
		  - **Replit**: Utilizes proprietary Firecracker/gVisor and custom internal SDKs.
		  - **Quora Poe**: Built on Modal AI SDK and sandbox infrastructure.
		  - **Restate**: Modal SDK for durable AI coding agents.
		  - **WeWeb, Bubble, Noloco**: Use Kubernetes and Docker ecosystem; integrate third-party AI SDKs optionally.
		  - **Rosebud AI**: Kubernetes and container orchestration with emerging microVM SDKs.
		  - **Factory.ai**: Agent-native development framework with custom SDK deeply integrated with container orchestration.
		  - **Microsandbox**: Microsandbox Rust SDK providing containerized isolated sandbox execution.
		  #+END_QUOTE
		- Some list of platforms using sandboxing environments: Lovable.dev, Bolt.new, Base44, Replit, Quora Poe, Restate, WeWeb, Bubble, Noloco, Rosebud AI, Factory.ai
		- Perplexity uses E2B
	- ### Conclusion
		- Modal, E2B, and container/microVM orchestrators dominating the market for secure, untrusted code execution at scale;
			- E2B & Modal have mature SDKs and greater isolation.
		- Other no-code platforms often started with generic public cloud containers (AWS Lambda, Vercel, Render) before moving to stricter sandboxing as customer code execution matured.
	- ### perpetual resource
		- https://github.com/restyler/awesome-sandbox
		  id:: 69748501-a5fe-4510-a7e1-c257de31cf54
- ---
- observability / capture tracing / telemtery framework for AI agents & LLM actions
	- OpenTelemetry-based for ditributed cloud tracing and telemetry
	- ?
	-
- → moved 2026-09-24: sandbox-provider landscape + Runloop fit now at wrk-wosoom doc-spec/feature/ai-sandbox.md