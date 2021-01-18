# resharkercli

## What is it?
Resharker is a tool for Jira and Git, combining common practices between each to automate typical tasks a developer has to do regularly.

It is also serving an incubating project for Kotlin multiplatform clients for the Jira REST API and Git system tools. I'm also using it as a playground for learning how to build a solid Kotlin/Native CLI.

## Why resharker?
Keeping Jira tickets up to date was once known as 'sharking' in the smaller team at my workplace, it's also a small riff on the name given to a popular set of .NET refactoring tools!

## Quick Start

Install the executable binary
```shell
git clone https://github.com/mherod/resharkercli.git && cd resharkercli && ./gradlew installBinary
```

Then run `resharkercli --help` from your project directory for a full list of options
