## [[2026-02-19]]

- **[[Tech Stack/Database/Managed Relational-Database providers]]**
    - common managed postges providers among startups: Amazon RDS/Aurora Postgres, Google Cloud SQL, Neon, Supabase;
- **[[Tech Stack/Database/ORM & SQL toolkits]]**
    - [x] [https://aarambhdevhub.medium.com/rust-orms-in-2026-diesel-vs-sqlx-vs-seaorm-vs-rusqlite-which-one-should-you-actually-use-706d0fe912f3](https://aarambhdevhub.medium.com/rust-orms-in-2026-diesel-vs-sqlx-vs-seaorm-vs-rusqlite-which-one-should-you-actually-use-706d0fe912f3)
        - [ ] [Rust ORMs in 2026: Diesel vs SQLx vs SeaORM vs Rusqlite (Full Comparison) - YouTube](https://www.youtube.com/watch?v=YkgaMnheiDM)
    - [x] [https://oneuptime.com/blog/post/2026-01-26-rust-postgresql-diesel-orm/view](https://oneuptime.com/blog/post/2026-01-26-rust-postgresql-diesel-orm/view)
    - **Conclusion**
        - ==Clorine== for rust interface generation from SQL files; It also validates SQL queries against a DB engine validator;

## [[2026-02-18]][[2026-02-19]] [[2026-02-20]] [[Tech Stack/Database/Schema Migration]]

- Database schema migration vs Database data migration (from one db to another);
- application-based migration vs database-first migration (separate db migration process utilizing db features and abstractions to serve multiple views) - both require coordinated app deployment remaining in-sync with database at some level;
    - App-first schema migraiton (Compatibility-driven / Expand-Contract)
        - The application layer owns compatibility. can either be implemented as separate migration or part of the app on startup.
        - Compatibility logic lives in: application code, feature flags, dual-read / dual-write logic;
        - can be applied on app startup migration where migration inseparable from app code; or migration can be applied as a job separately.
    - db-first migration (Multi-version schema / schema virtualization)
        - The database owns compatibility. one physical schema and multiple logical schema versions.
- deployment consideration - Apps are stateless while DBs are stateful;
- apply migrations sequentially and avoid parallel migrations (which would require locking mechanism);
- either use one of the popular DB migration tools or invent one (manual migration pipeline); The available tools some provide primitive functionality and others take over the entire aspect of the migration;
- Backward compatibility with existing running old app instances;
- **migrations strategies (migration patterns):**
  compared to Scheduled downtime (The maintenance window), online zero-migration doesn't interrupt service (in some approaches may cause data backfilling or locking interruptions);
    - offline Scheduled downtime (The maintenance window)
    - **Blue/Green application deployment** with manual online **Expand-Contract migrations strategy/pattern**
        - possibly: separate expand migration from contract migration stages into separate folder or migration versioning lines.
        - usually requires: migrations that are additive first and destructive later, code that tolerates both schemas, feature flags or compatibility shims, and often multiple app versions running at once.
    - automated expand/contract using coded database features
    - **pipeline stages example with refinery:**
        - considerations: tests on branch/shadow DB, schema diff validation, automated rollback strategy;
        - schema migration first, then validate the data:
            - schema expansion stage - doesn't break existing functionality;
            - Data backfill stage: move existing data into new structure (copy & validate data);
        - backend deployment to support new schema structure (deployed alongside old backend version);
        - after all traffic moved to new backend using loadbalancer, the old one is decommissioned;
        - schema finalization stage by cleaning up old schema (destructive action);
        - approach and strategy workflow summary:
            - > ## Zero-Downtime DB Migration — Concise Summary (Your Stack)
              > **Stack:** Rust Axum • PostgreSQL (Neon) • Refinery • GitHub Actions
              > **Model:** Forward-only migrations + dual backend versions + traffic shift.
              > ---
              > ## Core Rules
              > 1. **Forward-only:** No rollbacks. Fix via new migrations.
              > 2. **Never mix steps:** Schema, data, and cleanup must be separate.
              > 3. **Backend versions must always match the active schema.**
              > ---
              > ## The 4 Migration Stages
              > ### 1) Schema Expansion (Safe)
              > **DB:**
              > * Add new columns/tables/indexes only.
              > * No deletes/renames.
              > **Backend:**
              > * Old backend remains running.
              > **Purpose:**
              > Prepare new structure without breaking production.
              > ---
              > ### 2) Data Backfill
              > **DB:**
              > * Copy/transform old data into new schema.
              > * Run validation queries.
              > **Backend:**
              > * Still only old backend.
              > **Purpose:**
              > Ensure new schema is fully populated and correct.
              > ---
              > ### 3) Backend Cutover
              > **DB:**
              > * Both old and new schemas still exist.
              > **Backend:**
              > * Deploy new backend version.
              > * Gradually shift traffic from old → new.
              > **Purpose:**
              > Move application safely to the new schema.
              > ---
              > ### 4) Schema Finalization (Destructive)
              > **Trigger:**
              > Only after traffic = 100% on new backend.
              > **DB:**
              > * Drop old columns/tables.
              > **Backend:**
              > * Only new version remains.
              > **Purpose:**
              > Clean up legacy structures.
              > ---
              > ## Pipeline Flow (GitHub Actions)
              ```
              1. Run expansion migration
              2. Run backfill migration + validation
              3. Deploy new backend + shift traffic
              4. Run final cleanup migration
              ```
              > Each step gates the next.
              > ---
              > ## Why This Achieves Zero Downtime
              > At no point does running code depend on a missing schema:
              > * Old backend works with expanded schema.
              > * New backend starts only after data is ready.
              > * Destructive changes occur only after cutover.
              > ---
              > If needed, the next logical step is a **concrete GitHub Actions workflow + sample Refinery migrations**.
        - refinery is used to apply the migrations, but other tool or logic needs to create  the generate appropriate staged migrations for zero-downtime.
- modern migration stack: Declarative schema tool + zero-downtime migration engine;
    - **Declarative schema**
        - **Atlas**
            - Becoming the dominant enterprise tool
            - Drift detection + CI enforcement
        - **Safe migration execution**
            - **pgroll**
                - Used for zero-downtime rollouts
                - Supports phased migrations
- refinery (primitive tool) supports both data migration (update/backfill data) vs schema migrations. - incremental forward migrations only;
- postgres backups `pg_dump`  /  `pg_restore` - can be useful during migration workflow;
- Atlas: Modern; popular;
- Bytebase: very popular; encompasses a complete solution for DB changes (git fashion)
- Q: understand how integration between github and neon works; how to apply migrations on running instance; How to rollback migration; how to test migration before production.
- Q: how to coordinate the deployment of new app version with the database schema migration update (frontend, api server, database migration)?
- PARTIAL [read appropriate section only] [https://www.liquibase.com/blog/blue-green-deployments-liquibase](https://www.liquibase.com/blog/blue-green-deployments-liquibase)
    - [ ] [https://thorben-janssen.com/update-database-schema-without-downtime/](https://thorben-janssen.com/update-database-schema-without-downtime/)
- [ ] [https://www.liquibase.com/resources/guides/database-schema-migration](https://www.liquibase.com/resources/guides/database-schema-migration)
- LLM: What do teams rely on when using Github Actions for blue-green deployment?
    - > In practice, most teams **do not** build a database blue-green workflow from scratch entirely inside GitHub Actions. Instead, they use GitHub Actions as an **orchestrator** that triggers an external, specialized service to do the heavy lifting.
      > Creating a raw GitHub Action script to handle PostgreSQL data synchronization (logical replication, lag monitoring, and cutover) is extremely risky. If the script fails mid-way, you could end up with a split-brain scenario or data loss.
      > Here is how the responsibilities are typically split:
      > ### 1. The "Hybrid" Approach (Most Common)
      > Teams use GitHub Actions to manage the *lifecycle* but rely on a cloud provider (like AWS RDS) for the *data integrity*.
      > * **GitHub Actions Role:**
      > * Authenticates with the cloud provider (using OIDC/IAM).
      > * Triggers the "Create Blue/Green Deployment" API call.
      > * Runs application-level smoke tests against the **Green** (staging) endpoint.
      > * Prompts for manual approval (using GitHub Environments).
      > * Triggers the "Switchover" API call.
      > * **External Service Role (e.g., AWS RDS Blue/Green):**
      > * Handles the underlying PostgreSQL logical replication.
      > * Monitors replication lag to ensure 0% data loss.
      > * Handles the DNS swap during the cutover.
      > ### 2. The "Schema-First" Approach (No DB Swap)
      > Many high-velocity teams avoid swapping the entire database instance. Instead, they keep a single database but use a **Blue/Green application deployment** with **Expand-Contract migrations**.
      > * **Tools:** GitHub Actions + Liquibase, Flyway, or Atlas.
      > * **Workflow:**
      > 1. **Expand:** GitHub Actions runs a migration to add a new column (compatible with old and new code).
      > 2. **Deploy Green:** GitHub Actions deploys the new app version to a "Green" target group.
      > 3. **Contract:** Once the Green app is live and stable, a subsequent GitHub Action run removes the old column.
      > * **Why?** It's much faster than spinning up a whole new DB instance and avoids the complexities of cross-instance replication.
      > ### 3. Kubernetes-Native (The Operator Approach)
      > If you run Postgres on Kubernetes, teams often use an **Operator** (like CloudNativePG) and **ArgoCD**.
      > * **GitHub Actions Role:** Pushes a new container image or updates a Helm chart.
      > * **External Service (ArgoCD/Operator) Role:** Recognizes the change and orchestrates a "Rolling Update" or "Blue-Green" shift between Kubernetes pods. The Operator ensures the database remains healthy during the shift.
      > ---
      > ### Comparison: GitHub Actions vs. Managed Services
      > | Feature | Manual GitHub Actions Script | Managed Service (RDS/GCP) |
      > | --- | --- | --- |
      > | **Effort** | High (Writing complex shell/Python scripts) | Low (Single API call) |
      > | **Data Safety** | Low (Hard to handle edge-case failures) | High (Built-in lag monitoring) |
      > | **Cost** | Free (except for Runner minutes) | Usually an additional hourly fee |
      > | **Visibility** | Logs in GitHub Actions | Native monitoring dashboards |
      > **Verdict:** For the application layer, teams build the workflow in GitHub Actions. For the **PostgreSQL layer**, they almost always rely on an external managed service to ensure data consistency.
      > **Would you like me to show you a sample YAML for a GitHub Action that triggers an AWS RDS blue-green switchover?**
    - > For the application layer, teams build the workflow in GitHub Actions. For the **PostgreSQL layer**, they almost always rely on an external managed service to ensure data consistency.
    - > most teams **do not** build a database blue-green workflow from scratch entirely inside GitHub Actions. Instead, they use GitHub Actions as an **orchestrator** that triggers an external, specialized service to do the heavy lifting.
- [x] [The Ultimate Showdown of Database Migration Tools by Pasha Finkelshteyn / Anton Arhipov @ Spring I/O - YouTube](https://www.youtube.com/watch?v=U3QUxpmzxts)
- [ ] [https://docs.cloud.google.com/architecture/database-migration-concepts-principles-part-1](https://docs.cloud.google.com/architecture/database-migration-concepts-principles-part-1)
- [ ] [https://www.cloudbees.com/blog/database-migration](https://www.cloudbees.com/blog/database-migration)
- [ ] [https://www.striim.com/blog/an-introduction-to-database-migration-strategy-and-best-practices/](https://www.striim.com/blog/an-introduction-to-database-migration-strategy-and-best-practices/)
- [ ] [https://www.kinde.com/learn/ai-for-software-engineering/using-ai-for-your-databases/using-ai-agents-to-automate-database-schema-migrations-across-multi-tenant-saas-applications/#product](https://www.kinde.com/learn/ai-for-software-engineering/using-ai-for-your-databases/using-ai-agents-to-automate-database-schema-migrations-across-multi-tenant-saas-applications/#product)
- services: Liquibase, Flyway, or Atlas, prisma
- Q: how to achieve blue-green deployment strategy using tools like CircleCI, Jenkins, etc.; and Flyway, liquibase, etc. achieve it with application deployment; Neon? Flyio ?
- [x] [Introduction to Deployment Protection Rules - YouTube](https://www.youtube.com/watch?v=10J40Euwu7I&t=55s)
- [x] There are services that manage app feature-flags in frontend to enable on-demand features that use new backend/database schema;
- [x] [Blue–Green Deployment with Jenkins | Zero Downtime CI/CD Pipeline - YouTube](https://www.youtube.com/watch?v=5DRvfg1t3mE)
- [x] [CircleCI Beginner's Guide: CI/CD Made Easy - YouTube](https://www.youtube.com/watch?v=V_IWGjXSuPs)
- [x] [CI/CD Pipeline: Building a Real-World Example with CircleCI - YouTube](https://www.youtube.com/watch?v=Z90oD_ovLBg)
- [x] list of CI/CD services [https://blog.jetbrains.com/teamcity/2023/07/best-ci-tools/](https://blog.jetbrains.com/teamcity/2023/07/best-ci-tools/)
- [x] [An overview of how blue-green deployments work (with AWS examples) - YouTube](https://www.youtube.com/watch?v=DEd6jV3zRkU)
- modern dominant pattern:
  > Expand-and-contract migrations orchestrated via CI/CD, with traffic switching only after backward-compatible schema changes are applied.
- [LLM] most popular tools that handle database schema migrations?
- [x] [Database Migrations Explained - YouTube](https://www.youtube.com/watch?v=mMsZPZKNc4g)
- [x] [Migration Lesson: Don't Use Prisma | Prime Reacts - YouTube](https://www.youtube.com/watch?v=jqhHXe746Ns)
- Q: how to handle Graphql versioning ? and how to handle the corresponding database version;
- [x] [Postgres Schema Migrations - YouTube](https://www.youtube.com/watch?v=RqshK-VvjN4)
- [ ] [Andrew Farries - Postgres schema migrations using the expand/contract pattern (PGConf.EU 2024) - YouTube](https://www.youtube.com/watch?v=Wbg2HoYjLHU)
- List of common migration tools:
    - > For modern schema lifecycle management (Database DevOps), the landscape is dominated by "Open Core" tools—where the engine is open source but advanced orchestration is part of a paid tier.
      > To handle **blue-green deployments**, these tools must provide "Backward Compatibility Linting." This ensures that the schema change you're applying for the "Green" (new) environment doesn't break the "Blue" (old) environment while they both share the same database.
      > Here are the most commonly used open-source and open-core tools:
      > ### 1. Atlas (by Ariga)
      > Atlas is widely considered the most "modern" tool because it uses a declarative **Schema-as-Code** approach (similar to Terraform).
      > * **Open Source Status:** Open Core (Apache 2.0).
      > * **Blue-Green Capability:** It features a powerful **Migration Linting** engine. It can automatically detect "destructive" changes (like renaming a column) and warn you that it will break the "Blue" environment. It suggests safe alternatives, like adding a new column first and keeping the old one until the switch is complete.
      > * **Best For:** Modern DevOps teams using GitOps and Kubernetes.
      > ### 2. Flyway (by Redgate)
      > Flyway is the "industry standard" for version-based migrations. It is incredibly simple: you write SQL scripts, and it ensures they are executed in order.
      > * **Open Source Status:** Open Source (Community Edition) under Apache 2.0.
      > * **Blue-Green Capability:** While the community version is a "runner," it is commonly used in blue-green pipelines by manually splitting migrations into **Expand** (backward-compatible) and **Contract** (cleanup) scripts.
      > * **Best For:** Java/Spring Boot environments and teams who prefer writing raw SQL.
      > ### 3. Bytebase
      > Bytebase is a web-based "Database CI/CD" platform that provides a GUI for managing schema changes across multiple environments.
      > * **Open Source Status:** Open Core.
      > * **Blue-Green Capability:** It supports **custom rollout pipelines**. You can define a workflow where a schema change is applied, then wait for a manual "switch" before running the cleanup scripts. It also has built-in SQL linting to catch breaking changes.
      > * **Best For:** Large teams that need a centralized UI and collaborative reviews for database changes.
      > ### 4. Skeema
      > Skeema is specifically built for MySQL and MariaDB. Like Atlas, it is declarative—you define what the table *should* look like, and Skeema handles the `ALTER` statements.
      > * **Open Source Status:** Pure Open Source (Apache 2.0).
      > * **Blue-Green Capability:** It allows for "Online Schema Changes" (integrating with tools like `gh-ost` or `pt-online-schema-change`) which are critical for blue-green deployments to avoid table locks.
      > * **Best For:** MySQL-heavy environments that want a "diff-and-apply" workflow.
      > ### 5. Liquibase
      > Liquibase is the older, more "Enterprise" sibling to Flyway. It uses XML, YAML, or JSON to describe changes, making it database-agnostic.
      > * **Open Source Status:** Open Core / Functional Source License (FSL).
      > * **Blue-Green Capability:** It has the most robust **Rollback** support. If your "Green" environment fails, Liquibase can often automate the reversal of the schema changes to get "Blue" back to a healthy state.
      > * **Best For:** Large enterprises managing multiple types of databases (Oracle, SQL Server, Postgres, etc.) simultaneously.
      > ---
      > ### Comparison Summary
      > | Tool | Approach | License | Blue-Green Strategy |
      > | --- | --- | --- | --- |
      > | **Atlas** | Declarative (HCL/SQL) | Open Core | **Linting:** Prevents breaking changes automatically. |
      > | **Flyway** | Versioned (SQL) | OSS (Comm.) | **Manual:** Use "Expand/Contract" script naming. |
      > | **Skeema** | Declarative (SQL) | OSS | **Online:** Uses `gh-ost` for zero-downtime ALTERS. |
      > | **Bytebase** | UI / Pipeline | Open Core | **Workflow:** Dedicated stages for rollout and cleanup. |
      > | **Liquibase** | Change-sets | Open Core | **Rollback:** Superior safety for failed deployments. |
      > **Would you like me to explain the "Expand-Contract" pattern in detail so you can implement it with any of these tools?**
- [LLM] current (2024–2026 era) landscape of modern database migration / schema-management tools for modern DevOps + cloud-native workflows;
- example workflow: startups often use **Drizzle as the ORM** (to write queries in their app) but **Atlas as the Migration Engine**.
    - ​You define your schema in Drizzle (TypeScript).
    - ​**Atlas** reads that Drizzle schema.
    - ​**Atlas** generates the migration plan.
    - ​**pgroll** executes it to ensure your "Green" and "Blue" deployments don't crash during a column rename.
- [x] [https://pgroll.com/blog/how-to-perform-postgres-schema-changes-in-production-with-zero-downtime](https://pgroll.com/blog/how-to-perform-postgres-schema-changes-in-production-with-zero-downtime)
    - [x] [How to perform Postgres schema changes in production with zero downtime - YouTube](https://www.youtube.com/watch?v=-1aO6UznfI0) [Zero-downtime and reversible schema migrations with pgroll - YouTube](https://www.youtube.com/watch?v=Fcs-lmrVOQM)
- [x] [https://xata.io/blog/pgroll-schema-migrations-postgres](https://xata.io/blog/pgroll-schema-migrations-postgres)
- [x] [https://news.ycombinator.com/item?id=37752366](https://news.ycombinator.com/item?id=37752366)
- [x] [https://fabianlindfors.se/blog/schema-migrations-in-postgres-using-reshape/](https://fabianlindfors.se/blog/schema-migrations-in-postgres-using-reshape/)
- [ ] [https://www.bytebase.com/blog/top-open-source-postgres-migration-tools/](https://www.bytebase.com/blog/top-open-source-postgres-migration-tools/)
- [ ] [https://airbyte.com/data-engineering-resources/database-schema-migration](https://airbyte.com/data-engineering-resources/database-schema-migration)
- [x] [https://www.reddit.com/r/devops/comments/1nrr46l/zero_downtime_deployments_with_database_migrations/](https://www.reddit.com/r/devops/comments/1nrr46l/zero_downtime_deployments_with_database_migrations/)
- [x] [https://www.reddit.com/r/PostgreSQL/comments/1newev8/pgschema_postgres_declarative_schema_migration/](https://www.reddit.com/r/PostgreSQL/comments/1newev8/pgschema_postgres_declarative_schema_migration/)
- [x] [https://users.rust-lang.org/t/why-use-diesel-when-its-not-async/90160](https://users.rust-lang.org/t/why-use-diesel-when-its-not-async/90160)

## [[2026-02-17]]

- [x] [How do software projects achieve zero downtime database migrations? - YouTube](https://www.youtube.com/watch?v=cw5K2O4AHJc)
    - [ ] [https://www.prisma.io/dataguide/types/relational/expand-and-contract-pattern](https://www.prisma.io/dataguide/types/relational/expand-and-contract-pattern)
    - Compared to locking a database to process a migration in one go, expand & contract required multi step deployment that involves also application code;
        - requiring development effort to manage dual-writes, backfills, and feature flags;

## [[2026-02-08]]

- 
    - Neon: The most popular choice for new startups due to its serverless model, generous free tier, and tight integration with modern platforms like Vercel.
    - Supabase: Extremely popular among developers as an all-in-one backend where managed Postgres comes bundled with auth, storage, and APIs.
    - Heroku Postgres: Long-standing and widely used managed Postgres with a large legacy user base, but relatively expensive today.
    - AWS RDS / Aurora PostgreSQL: The most widely deployed managed Postgres by sheer volume, dominant in enterprises but not cost-optimized for small teams.
    - Aiven: Respected but niche managed Postgres provider, popular with teams prioritizing reliability over lowest cost.
    - Crunchy Bridge: A smaller but credible option backed by PostgreSQL experts, used mainly by teams that want managed Postgres without a full platform lock-in.
- neon.tech
    - supports also auth database implementation based on [https://www.better-auth.com/community](https://www.better-auth.com/community)
- planetscale.com
    - [x] [https://planetscale.com/blog/planetscale-for-postgres](https://planetscale.com/blog/planetscale-for-postgres)
- [https://www.convex.dev](https://www.convex.dev) realtime database and authentication services; complete backend solution for app developers;
- **established postgres products**
    - > Amazon Aurora
      > AlloyDB
      > Lakebase/Neon
      > Supabase
      > Heroku
      > Crunchy Data
      > TigerData
- [x] [CI/CD for Databases with Demo | GitHub Actions + Neon | Postgres DB | Neon Ephemeral branches - YouTube](https://www.youtube.com/watch?v=aAOV8WpBnes)
    - CI/CD using ephemeral database snapshots: create a dev branch from production main database, then for each CI/CD step create an ephermeral database to that specific session/test;
- [x] [Ship better apps with this CI/CD flow - YouTube](https://www.youtube.com/watch?v=6XezQQJGdjI)
- Q: does Neon support custom postgres containers/Dockerfile?
- **conclusion**
    - ==neon== (popular choice for small vibe-coded/simple projects) or ==platnetscale== (newer solution; hardware optimized; cheap): ^neon-popular-choice-for-small-vibe-coded-simple-projects-or
- Older
    - **Resources:**
        - [x] [https://www.restack.io/docs/supabase-knowledge-supabase-kubernetes-integration](https://www.restack.io/docs/supabase-knowledge-supabase-kubernetes-integration)
        - [x] [Self-Host Supabase with Kubernetes: A Simple Guide](https://medium.com/@andersonk/self-host-supabase-with-kubernetes-a-simple-guide-438c415ebd54)
        - [x] [https://www.reddit.com/r/Supabase/comments/1crpuko/self_hosting_on_kubernetes_database_storage/](https://www.reddit.com/r/Supabase/comments/1crpuko/self_hosting_on_kubernetes_database_storage/)
        - [x] [https://www.reddit.com/r/Supabase/comments/1cn1nph/rant_is_it_really_opensource/](https://www.reddit.com/r/Supabase/comments/1cn1nph/rant_is_it_really_opensource/)
        - [x] [How to set up Supabase with Kubernetes and Helm? | Bootstrapped Supabase Guides](https://bootstrapped.app/guide/how-to-set-up-supabase-with-kubernetes-and-helm)
        - [x] [How to use Supabase with Kubernetes? | Bootstrapped Supabase Guides](https://bootstrapped.app/guide/how-to-use-supabase-with-kubernetes)
        - [x] [Self-hosting Supabase on Ubuntu with Digital Ocean](https://www.youtube.com/watch?v=FqiQKRKsfZE)
        - [ ] [Self-Hosting | Supabase Docs](https://supabase.com/docs/guides/self-hosting)
            - [ ] [StackGres | Running Supabase on Top of StackGres](https://stackgres.io/blog/running-supabase-on-top-of-stackgres/)
            - [x] [Self-Hosting with Docker | Supabase Docs](https://supabase.com/docs/guides/self-hosting/docker)
            - [ ] [GitHub - supabase-community/supabase-kubernetes: Helm 3 charts to deploy a Supabase on Kubernetes](https://github.com/supabase-community/supabase-kubernetes)
        - [x] [Supabase Explained](https://www.youtube.com/watch?v=T-qAtAKjqwc)
        - [x] [Supabase: Honest review after building 3 startups](https://www.youtube.com/watch?v=SYhfdWPMcSY)
        - [ ] [Learn Supabase (Firebase Alternative) – Full Tutorial for Beginners](https://www.youtube.com/watch?v=dU7GwCOgvNY)
        - [ ] [Supabase Crash Course](https://www.youtube.com/watch?v=7uKQBl9uZ00)
        - [x] [StackGres: Cloud-Native PostgreSQL on Kubernetes / Álvaro Hernández (OnGres)](https://www.youtube.com/watch?v=0fnJ6biCDm4)
            - [x] [OnGres: Sharding Postgres on Kubernetes | DoK Ecosystem Day](https://www.youtube.com/watch?v=DqnXgt8R7Qg)
        - [x] [I replaced my entire tech stack with Postgres...](https://www.youtube.com/watch?v=3JW732GrMdg)
        - [x] [Integrating Patroni PostgreSQL with Kubernetes](https://www.youtube.com/watch?v=iw98cGVf_sI)
        - [x] [How to run a basic PostGreSQL in Kubernetes!](https://www.youtube.com/watch?v=iYwE3h0p7Zs)
        - [ ] [Step by Step Application Deployment on LKE using Helm | Kubernetes on Cloud (2/2)](https://www.youtube.com/watch?v=JGtJj_nAA2s&t=0s)
        - [x] [https://www.reddit.com/r/kubernetes/comments/139ahop/what_are_you_using_to_run_postgres/](https://www.reddit.com/r/kubernetes/comments/139ahop/what_are_you_using_to_run_postgres/)
        - [x] [https://www.reddit.com/r/kubernetes/comments/1fzk9nl/best_production_grade_postgres_operator_for/](https://www.reddit.com/r/kubernetes/comments/1fzk9nl/best_production_grade_postgres_operator_for/)
        - [ ] ~ [technical debt of Supabase] [Supabase versus Magic - You win! | AINIRO.IO](https://ainiro.io/blog/supabase-versus-magic-you-win)
        - [x] +++ [How to choose your Kubernetes Postgres Operator?](https://www.simplyblock.io/blog/choosing-a-kubernetes-postgres-operator/)
        - [x] [CloudNativePG: Kubernetes Databases Made Simple (Full Course)](https://www.youtube.com/watch?v=g59ki9z2SO8)
        - [ ] [Should We Run Databases In Kubernetes? CloudNativePG (CNPG) PostgreSQL](https://www.youtube.com/watch?v=Ny9RxM6H6Hg&t=507s)
        - [x] [https://www.reddit.com/r/kubernetes/comments/1g8v4bg/which_storage_solution_for_cnpg/](https://www.reddit.com/r/kubernetes/comments/1g8v4bg/which_storage_solution_for_cnpg/)
        - [x] [High Availability Setup with CNPG PostgreSQL Operator](https://support.tools/high-availability-setup-cnpg-postgresql-operator/)
        - [ ] [Optimizing Databases on Kubernetes: Kubernetes Backup and Recovery with CNPG and ZFS Snapshots](https://www.ardanlabs.com/blog/2025/01/optimizing-databases-on-kubernetes-ep5-kubernetes-backup-and-recovery-with-cnpg-and-zfs-snapshots.html)
        - [x] [How to Design a Database](https://www.youtube.com/watch?v=5RpUmDEsn1k&ab_channel=DatabaseStar)
        - [ ] ~ [https://www.reddit.com/r/Database/comments/1hj71u0/graph_databases_are_not_worth_it/](https://www.reddit.com/r/Database/comments/1hj71u0/graph_databases_are_not_worth_it/)
        - [x] [Inheritance and polymorphism: where the cracks in SQL begin to show](https://typedb.com/blog/inheritance-and-polymorphism-where-the-cracks-in-sql-begin-to-show)~~ - why is polymorphic entity design in SQL not solved yet? is there a feature missing?~~
        - [x] [Understanding Database Design with Real-World Scenarios](https://rahultiwari876.medium.com/understanding-database-design-with-real-world-scenarios-8b21516ce485)
        - [x] [Top 11 Real-world SQL Applications: Building Real-world SQL Projects](https://kockpit.in/blogs-community/article.php?q=top-11-real-world-sql-applications:-building-real-world-sql-projects)
        - [ ] [7 Useful Database Diagram Examples](https://vertabelo.com/blog/database-diagrams-examples/)
        - [ ] [4 Real-World SQL Database Schema Examples](https://www.dragonflydb.io/databases/schema/sql)
        - [ ] [Database schema templates by DrawSQL](https://drawsql.app/templates)
        - [ ] drawing models [https://www.reddit.com/r/selfhosted/comments/1gv2p2j/chartdb_v120_opensource_database_diagram/](https://www.reddit.com/r/selfhosted/comments/1gv2p2j/chartdb_v120_opensource_database_diagram/)
        - [x] [CloudNativePG - PostgreSQL extensions](https://www.dbi-services.com/blog/cloudnativepg-postgresql-extensions/)
        - [ ] ~ [TODO: add example scripts to project] [CloudNativePG – The kubectl plugin](https://www.dbi-services.com/blog/cloudnativepg-the-kubectl-plugin/)
        - [x] [Creating a custom container image for CloudNativePG](https://cloudnative-pg.io/blog/creating-container-images/)
        - [x] [The Immutable Future of PostgreSQL Extensions in Kubernetes with CloudNativePG](https://www.gabrielebartolini.it/articles/2025/03/the-immutable-future-of-postgresql-extensions-in-kubernetes-with-cloudnativepg/)
        - [ ] [The best solution I've ever seen is this Rust library [https://github.com/cornuco](https://github.com/cornuco)... | Hacker News](https://news.ycombinator.com/item?id=34282120)
        - [x] [https://www.reddit.com/r/rust/comments/10tsjam/thoughts_about_switching_from_sqlx_to_tokio/](https://www.reddit.com/r/rust/comments/10tsjam/thoughts_about_switching_from_sqlx_to_tokio/)
        - [x] [Use Postgres with Rust - Waldeinsamkeit](https://www.city17.xyz/tokio-postgres/)
        - [ ] ~ [https://www.reddit.com/r/rust/comments/1jnd5bh/sqlx_slows_down_axum_by_30x/](https://www.reddit.com/r/rust/comments/1jnd5bh/sqlx_slows_down_axum_by_30x/)
            - [ ] ~ [https://old.reddit.com/r/rust/comments/1btdrc1/does_sqlx_really_have_more_overhead_in_rust_than/](https://old.reddit.com/r/rust/comments/1btdrc1/does_sqlx_really_have_more_overhead_in_rust_than/)
    - **Qurstions**
        - Kubernetes Jobs?
        - custom migration framework (built in-house): run scripts & track migraitons
        - Data migration between updates?
        - The issue with database migrations is that the application and database schema are both tightly coupled. The version of the app is made to work with a certain schema
        - There are couple of patterns to achieve synchronized app with database schema versioning as well as zero-downtime deployment. Deployment is decoupled from migration with careful ordering and possible multistage roll-out in production.
            - expand-contract (online migration pattern) - most sophistecated
        - **production-grade rollout strategies** for setup using Rust + Refinery + CNPG + Kubernetes?
            - Migration strategies/approaches
                - Minimal downtime migration
                - Expand-contract pattern approach.
                    - change should be backward compatible (either when following schema-first or code-first migration strategy)
            - CI/CD integration patterns
                - Kubernetes custom operator to ensure schema + app versions match declaratively - migrating DB before deploying app or in-tandem through multiple stages.
                - Migration Job
            - App-Aware Version Check to prevent incompatible versions to run.
            - Do not use undo migrations instead always follow forward migrations when requiring rollback changes.
    - **Resources:**
        - [x] [https://www.zupzup.org/rust-db-migrations/index.html](https://www.zupzup.org/rust-db-migrations/index.html)
        - [x] [https://news.ycombinator.com/item?id=21752410](https://news.ycombinator.com/item?id=21752410)
        - [ ] [https://news.ycombinator.com/item?id=37724549](https://news.ycombinator.com/item?id=37724549)
        - [x] [ Database Migrations for Beginners | Flyway Tutorial ](https://www.youtube.com/watch?v=dJDBP7pPA-o)
        - [ ] [ Updating Databases the GitOps Way - Kostis Kapelonis, Codefresh & Rotem Tamir, Ariga ](https://www.youtube.com/watch?v=U-o4HUNGHsE)
        - [ ] [ Database Migration Strategies & Flyway Example ](https://www.youtube.com/watch?v=_lAjBOXhrXY)
        - [x] [ Database migrations - بالعربي ](https://www.youtube.com/watch?v=TmVRKDoMulE)
