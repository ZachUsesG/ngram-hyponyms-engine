# NGram Hyponyms Engine

A graph-based Java application that merges **WordNet semantic networks** with **Google N-Grams frequency data** to explore how word meanings and usage evolve over time.

## Overview

The NGram Hyponyms Engine models language as both a **graph** and a **timeline**.  
By connecting WordNet synsets through hyponym relationships and weighting those connections by historical N-Gram frequencies, the system allows users to query a term and uncover how its conceptual space and usage have shifted across decades.

## Core Features

- **WordNet Graph Traversal:** Builds a directed acyclic graph of synsets and hyponyms for recursive semantic lookups.  
- **Frequency-Weighted Analysis:** Integrates Google N-Gram counts to rank or filter hyponyms based on cultural prominence.  
- **Temporal Querying:** Allows multi-word and time-bounded queries for nuanced linguistic exploration.  
- **Modular Architecture:** Clean separation between data ingestion, graph logic, and visualization layers.

## Technical Stack

| Layer | Description |
|-------|--------------|
| **Language** | Java 17 |
| **Frameworks** | Custom-built graph and frequency engines |
| **Data** | WordNet synset and hyponym files, Google N-Gram CSVs |
| **Frontend** | Lightweight JS/CSS visualization hosted locally through `ngordnet.html` |
| **Testing** | JUnit suite covering traversal, I/O, and query logic |

## Repository Structure

src/ → Core Java source files
data/ → WordNet and N-Gram datasets (TXT, CSV)
static/ → Front-end HTML, CSS, and JS files
tests/ → Unit tests

bash
Copy code

## Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/ZachUsesG/ngram-hyponyms-engine.git
   cd ngram-hyponyms-engine
Open the project in IntelliJ IDEA or another Java IDE.

Run Main.java in src/main/.

Open static/ngordnet.html in a browser to visualize query results.

Example Use Case
Query: “bird”
→ Returns hyponyms such as sparrow, eagle, crow
→ Displays frequency-weighted trends showing how each term’s prominence has changed over time.

Purpose
Developed as a hybrid of computational linguistics and data visualization, this project reflects my interest in building intelligent systems that make large datasets intuitive and explorable. It also demonstrates end-to-end software design from data parsing to interface delivery.

Credits
Created by Zach Makari
WordNet data © Princeton University
Google N-Gram data © Google Research
