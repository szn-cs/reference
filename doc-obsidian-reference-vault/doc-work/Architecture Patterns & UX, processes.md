---
description: cloud architecture fundamentals, design patterns, etc.
---

- SaaS application at minimum - A real-world SaaS application has, at a minimum, the following components:
    - **User interface** (an HTML form or basic frontend) - An HTML form and a database; typically a relational database.
        - **Server-side logic** to process requests.
        - **Persistent storage** (usually a relational database).
        - **User authentication** (accounts + sessions).
    - The ability to handle media files or documents, generally using S3.
    - A multi-user and realtime component, where changes made by one person in one location are reflected instantly in the browser of another person. (serving multiple users, sharing state, expect instant feedback).

### misc. notes:

- hosting frontend can be different than hosting backend;
- jamstack.org static-first decoupled web architecture where prebuilt files are served from a CDN and dynamic behavior is provided via client-side JavaScript calling APIs.
    - e.g. SPA

---
# system components:

- networking stack
    - **loadbalancer**
    - **BGP/closest-node anycast routing** (routing protocol): single IP address announced from multiple geographically distributed servers. Anycast is a core internet routing mechanism that connects clients to the “nearest” server advertising a block of IPs.
        - simple mechanism where global routing infrastructure chooses the most optimal server responder using internet routing network layer.
    - DNS records
    - CDN

# UX

- **on boarding flow.**

---
# product process for AI builder tools:

- A
    - **Conduct Research:** **Find out what people need.** Ask them about their problems and look at other apps/tools that already exist.
    - **Plan & Strategize:** **Figure out the best plan.** Based on your research, see where your idea is weak, and where you have a chance to do something better than others. Create flow charts.
    - **Design UI:** **Make a small test version.** Build just the most important part of the app so you can test it right away and fix things quickly.
    - **No-code the app:** **Build the final product.** Put together the look of the app, set up how the data is stored (the "brain"), and add all the features using no-code tools.
- B
    - Layout -> design -> animations -> code

---
# Agentic systems:

- **Workflow**
    - Chaining LLMs
    - **Orchestrated workflows**: LLM divides prompt into parallel sub-tasks.
        - ![[asset/8985fc683fae4780fb34eab1365ab78c7e51bc8e-2401x1000.webp|1088x501]]
        - coding making complex changes to multiple files each time would benefit from orchestrator pattern where the LLM dynamically breaks down tasks, delegates to worker LLMs, and then synthesizes their results.
- **Agents:**
    - **Agents using tools based on environmental feedback in a loop**: independent LLM operation + environment tools feedback in a loop (operates in many turns); with complex inputs, LLMs engage in reasoning & planning, then operating independently while using environment tools as ground truth to assess progress.
        - ![[asset/58d9f10c985c4eb5d53798dea315f7bb5ab6249e-2401x1000.webp|1040x409]]
        - **High-level flow of a coding agent**
            - Agent design is effective for coding because they can iterate on solutions using test results as feedback (verifiable through automated tests); The problem space is well-defined and structured; Output quality can be measured objectively;
            - ![[asset/4b9a1f4eb63d5962a6e1746ac26bbc857cf3474f-2400x1666.webp|1286x874]]
        - most value for tasks that require both conversation and action, have clear success criteria, enable feedback loops, and integrate meaningful human oversight;
            - pay attention to creating good *agent*-computer interfaces (ACI i.e. tools documentation) to match models capabilities & limitations.
        - Prompts:
            - system prompt
            - Tool definitions and specifications prompt: good tool definition often includes example usage, edge cases, input format requirements, and clear boundaries from other tools.
                - write testing in [https://console.anthropic.com/workbench](https://console.anthropic.com/workbench)
        - 2 principles to maintain for AI agents:
            - Share context between agents, and share full agent traces (tools called and decisions made), not just individual messages.
            - Actions carry implicit decisions (each agent tasked fills gaps with assumptions), and conflicting decisions carry bad results.
            - ![[asset/836a7407ddf3dfacc0715c0502b4f3ffc7388829-1406x1230.png|951x807]]
