# Contributing to OpenRD
Thank you for having an interest in contributing to OpenRD! This document will guide you through the 
details and expectations of how to contribute in various ways. 

## Core Principle
The core principle behind the OpenRD project is "Ensure Understanding, not just Functionality". The ultimate goal of the
project is to provide a comprehensive way to understand the Communication Protocol of Rudia Controllers. This means that
it is not enough to have a project in a state where it "just works". Rather, OpenRD should allow core concepts of the
`.rd` file format and Rudia Communication Protocol to be fully understandable from the project alone.

## Development
As per the core principles above, there are a few requirements for developers intending to contribute to OpenRD. They 
are as follows: 

### Commit Etiquette
Commits should be atomic and broken down into small changes. Large PRs with a significant number of line changes in 
singular commits are difficult to review / quality check, and hence are likely to be rejected. 

### Contributions should not utilise LLM tools 
This project is one built around understanding and contextualisation of the Communication Protocol of Rudia Controllers. 
As such, the utilisation of LLM tools is strictly not allowed for any work within this project. 

A good example of why can be seen in the initial commit version of `RDUdp.java`. An LLM was utilised to quickly translate
the Python API version to a Java equivilant due to time constraints. Upon review a week later, I (senft-research) was 
unable to understand any of said code, and had to remake it from scratch. LLMs are notorious for tricking developers
into thinking they understand content, but in reality leave you with significant cognitive debt. This goes directly against
the principles of this project, hence LLM tools are banned.

### Issues and PRs
For every PR someone wishes to contribute, there should be a respective issue opened, relaying the problem the PR is trying
to address. Issues should be made first, along with intention to contribute a solution to the Issue, and PRs should be labeled
branches citing the issue name. For example `bugfix-1123` for a corresponding ticket for `issue-1123`.

