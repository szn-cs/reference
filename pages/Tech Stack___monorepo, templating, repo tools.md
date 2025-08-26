# Scripting  & Monorepo task execution pipelining tools & build systems :
	- considerations:
		- ***m***anagement, ***o***rganization, ***o***rchestration
		- task runner; Monorepo management tool
	- unmaintained [GitHub - DanielKeep/cargo-script: Cargo script subcommand](https://github.com/DanielKeep/cargo-script)
	- [GitHub - igor-petruk/scriptisto: A language-agnostic "shebang interpreter" that enables you to write scripts in compiled languages.](https://github.com/igor-petruk/scriptisto?tab=readme-ov-file)
	- library [GitHub - matklad/xshell](https://github.com/matklad/xshell)
	- 👍 rust-script [GitHub - fornwall/rust-script: Run Rust files and expressions as scripts without any setup or compilation step.](https://github.com/fornwall/rust-script): most mature with more features.
	- library [GitHub - rust-shell-script/rust_cmd_lib: Common rust command-line macros and utilities, to write shell-script like tasks in a clean, natural and rusty way](https://github.com/rust-shell-script/rust_cmd_lib)
	- RFC for cargo script command that executes rust as bash script [Tracking Issue for cargo-script RFC 3424 · Issue #12207 · rust-lang/cargo](https://github.com/rust-lang/cargo/issues/12207)
	- Werf for Kubernetes [GitHub - werf/werf: A solution for implementing efficient and consistent software delivery to Kubernetes facilitating best practices.](https://github.com/werf/werf)
	- Nx [GitHub - nrwl/nx: An AI-first build platform that connects everything from your editor to CI. Helping you deliver fast, without breaking things.](https://github.com/nrwl/nx?utm_source=monorepo.tools)
	- Turborepo [Guides | Turborepo](https://turborepo.com/docs/guides)
	- Bazel (overly complex, specific for Google scale issues)
		- Complex! simple are not trivial to achieve -> will waste a lot of time configuring the tool.
			- Because of contributor friction, Kubernetes dropped support for Bazel in their project - but still uses it somehow.
		- dependency hell problem - updating a single version can break others (requiring a chain of updates & changes which are not handled automatically).
		- With all these issues, Bazel seems to be the only tool powerful enough to cover all usecases. It also solves dependency context issues when building with skaffold & docker.
	- [A developer productivity tooling platform. | moonrepo](https://moonrepo.dev/)
	- [GitHub - moonrepo/moon: A build system and monorepo management tool for the web ecosystem, written in Rust.](https://github.com/moonrepo/moon?utm_source=monorepo.tools)
	- candidates:
		- Nx (uses Bazel internally), Moon, Bazel (Java-based, complex setup and customization, featureful);
	- wasm pluggin system [Overview | Extism - make all software programmable. Extend from within.](https://extism.org/docs/overview)
	- [Bazel modules](https://bazel.build/external/module)
	- Nx
	-
	- ## Conclusion
		- cargo workspaces, pnpm workspaces, shell scripts, rust scripts, with separate toolings like skaffold are most flexible and straight forward to implement, although their maintenance could be tedious & manual when creating new or refactoring but it is clear and easy.
			- I don't see that any monorepo manager is worth the effort. Non have complete sensible support to skaffold without requiring large config and changes;
		- Use rust scripts in each monorepo component's "script" folder, with main.rs as a REPL -like (scratchpad binary) to execute the requested commands.
-
- # Tools for repo management:
	- TODO renovate https://docs.renovatebot.com/ - updated dependencies across languages & microservices.
		- replaces dependentbot github
	- Nix flakes  - reproducable toolchains and specific version cli commands across dev environment. {{video https://www.youtube.com/watch?v=5D3nUU1OVx8}} - reproducable environments.
	- the Nix language is hard to learn. Is there a Rust alternative for Nix?
-
- # tools for secrets:
	- use **Mozilla SOPS** or **git-crypt** to encrypt tokens into repo.
	- use direnv in addition to dotenv as an automated way to load env into current folder.
	-
- # misc tools:
	- REPL for Rust https://github.com/evcxr/evcxr/tree/main/evcxr_repl
-
-
-
	- Categories of tools:
		- #+BEGIN_QUOTE LLM
		  | Category                    | Role                                    | Examples               |
		  | --------------------------- | --------------------------------------- | ---------------------- |
		  | Monorepo Managers           | Coordinate multi-lang builds & tasks    | Nx, TurboRepo, Pants   |
		  | Container Build & Deploy    | Kubernetes app lifecycle management     | Skaffold, Tilt, Helm   |
		  | Package Managers            | Dependency management per language      | Cargo, pnpm            |
		  | CI/CD Orchestration         | Automate testing & deployment           | GitHub Actions, Tekton |
		  | Code Quality & Testing      | Enforce standards & run tests           | Clippy, ESLint, Jest   |
		  | Infrastructure as Code      | Kubernetes manifests & infra automation | Kustomize, Terraform   |
		  | Secret & Config Management  | Manage secrets securely                 | Vault, Sealed Secrets  |
		  | Observability & Monitoring  | Health & performance tracking           | Prometheus, Grafana    |
		  | Dependency Caching          | Speed builds & reduce network overhead  | Nx Cloud, Cargo proxy  |
		  | Developer Environment Setup | Reproducible dev environments           | DevContainers, Direnv  |
		  #+END_QUOTE
-
- ### resources:
	- TODO ~[Pitfalls When Adding Turborepo To Your Project](https://engineering.caribouwealth.com/blog/hello-turborepo)
	- TODO ~[Is Turborepo overhyped? | Tolgee](https://docs.tolgee.io/blog/turborepo-overhyped)
	- TODO [Why we chose Turborepo](https://medium.com/@scriptmind.corp/why-we-chose-turborepo-pending-c28bfc89ca89)
	- TODO [Building a Rust workspace with Bazel](https://www.tweag.io/blog/2023-07-27-building-rust-workspace-with-bazel/)
	- TODO [Bazel - Rust Project Primer](https://rustprojectprimer.com/build-system/bazel.html)
	- TODO [Building a Monorepo with Rust](https://earthly.dev/blog/rust-monorepo/)
	- TODO [Introduction | Blaze](https://blaze-monorepo.dev/docs/introduction/)
		- TODO [https://www.reddit.com/r/rust/comments/1feytpt/repost_blaze_a_simple_and_flexible_monorepo_based/](https://www.reddit.com/r/rust/comments/1feytpt/repost_blaze_a_simple_and_flexible_monorepo_based/)
	- TODO [Languages | moonrepo](https://moonrepo.dev/docs/how-it-works/languages)
	- DONE [Monorepo Explained](https://monorepo.tools/)
	- TODO [https://www.reddit.com/r/devops/comments/1ey1c1w/monorepo_users_what_tools_do_you_use/](https://www.reddit.com/r/devops/comments/1ey1c1w/monorepo_users_what_tools_do_you_use/)
	- DONE [Bazel vs Nx vs moon](https://bejamas.com/compare/bazel-vs-nx-vs-moon)
	- DONE [Monorepo Tools: A Comprehensive Comparison](https://graphite.dev/guides/monorepo-tools-a-comprehensive-comparison)
	- DONE ~~Nx baedo on Bazel ~~[Nx ❤️ Bazel: Release Plans](https://blog.nrwl.io/nx-%25EF%25B8%258F-bazel-release-plans-eaed5bbaa3ba)
	- DONE [https://www.reddit.com/r/devops/comments/zncba6/life_after_nx/](https://www.reddit.com/r/devops/comments/zncba6/life_after_nx/)
	- TODO ~[Ask HN: Best Tools for Monorepo? | Hacker News](https://news.ycombinator.com/item?id=41120662)
		- TODO [GitHub - korfuri/awesome-monorepo: A curated list of awesome Monorepo tools, software and architectures.](https://github.com/korfuri/awesome-monorepo)
	- DONE [Monorepos don't HAVE to be hard (...and why you should always use them)](https://www.youtube.com/watch?v=SOWx0vhoEGI&ab_channel=JoshuaMorony)
	- TODO [How Skaffold Enabled Me to Deliver Features 10 Times Faster](https://medium.com/wix-engineering/how-skaffold-enabled-me-to-deliver-features-10-times-faster-aa2378c90508)
	- TODO [Learn Turborepo By Example | Code Sharing, Distributed Cache, & More!](https://www.youtube.com/watch?v=nx-cj3dqWNE&ab_channel=MichaelGuay)
	- TODO [The best of both worlds with Nix + Bazel](https://www.youtube.com/watch?v=FoSCSQO5xhI&ab_channel=TheLinuxFoundation)
	- TODO [Building with Bazel, Episode 1: Introduction](https://www.youtube.com/watch?v=53EnTaXdySk&list=PL23Revp-82LK5Xvy_iQYSacLZ6zIyBGZmX&ab_channel=Kodeco)
	- TODO [Monorepos with Bazel](https://www.youtube.com/watch?v=8atALH-kbI8&ab_channel=DiegoPacheco)
	- TODO [Unlocking Bazel's Potential with Monorepo: A Key to Efficient Software Development](https://www.youtube.com/watch?v=5LiLOSxP8bQ&ab_channel=TheLinuxFoundation)
	- TODO [Using Gazelle to Improve Multi-Language Bazel Monorepo](https://www.youtube.com/watch?v=MUP35hfK0q4&ab_channel=KrisFoster)
	- TODO [Bazel: {Fast, Correct, Seamless}: Choose 3 - Srini Muthu, LinkedIn](https://www.youtube.com/watch?v=LfsykTj38SY&ab_channel=TheLinuxFoundation)
	- TODO [Bazel-lib for BUILD and rules authors](https://www.youtube.com/watch?v=KoXN07MdIuo&ab_channel=TheLinuxFoundation)
	- TODO [Bazel - Platforms explained](https://www.youtube.com/watch?v=CrfQaZjuj_Y&ab_channel=Blaizard)
	- TODO [Skaffold a new way for deploying applications to k8s clusters](https://medium.com/@michamarszaek/skaffold-a-new-way-for-deploying-applications-to-k8s-clusters-f8ed4f2539a9)
	- TODO [How Skaffold Enabled Me to Deliver Features 10 Times Faster](https://medium.com/wix-engineering/how-skaffold-enabled-me-to-deliver-features-10-times-faster-aa2378c90508)
	- TODO [https://www.reddit.com/r/devops/comments/1c2g3s4/bazel_is_ruining_my_life/](https://www.reddit.com/r/devops/comments/1c2g3s4/bazel_is_ruining_my_life/)
	- TODO [Bazel](https://skaffold.dev/docs/builders/builder-types/bazel/)
	- TODO [🚀 Full-Stack Blog App with NestJS, NextJS & Turborepo – 10-Hour Ultimate Guide](https://www.youtube.com/watch?v=rsRglxTKbR0&ab_channel=SakuraDev)
-
- ---
- # Templating tool:
	- [ask llm] is there a scaffolding, code generation, or project templating tool that allows to insert bunch of configs and files to a framework project depending on which features are enabled or what components are enabled? The tool must also work by supporting to augment existing projects.