# JobScheduler
This project is a Java-based job scheduling system designed to manage and schedule jobs with dependencies. It ensures efficient execution order and detects potential cycles within the job graph. The system uses dynamic arrays for flexible job management and provides real-time feedback on job start times and overall schedule completion.

Features:

- Job Insertion and Management: Add jobs with specific durations and define dependencies to ensure correct execution order.
- Topological Sorting and Cycle Detection: Determine the correct order of job execution and detect cycles in the job graph.
- Dynamic Array Implementation: Manage jobs and dependencies efficiently with automatic resizing.
- Shortest Path Calculation: Compute the earliest possible start times for jobs and the overall finish time using shortest path algorithms in a Directed Acyclic Graph (DAG).
- Real-Time Feedback: Provide users with immediate updates on job scheduling and potential issues.

Classes and Methods:

Schedule Class:

1) Manages the job scheduling system.
Methods: insert(int time), get(int index), finish(), findTopologicalOrder(), dagShortestPath(), resetFields().

2) Job Class:
Represents a job with properties like start time, duration, dependencies, and cycle status.
Methods: requires(Job from), start().

3)DynamicArray<T> Class:
Implements a dynamically resizing array for job management.
Methods: add(Object obj), get(int index), clear(), isEmpty(), indexOf(Object obj), size().

Usage:

1) Insert jobs and specify their durations.
2) Define job dependencies using the requires method.
3) Calculate the schedule finish time using the finish method.
4) Retrieve job start times and manage the job execution order.
Outcome:
5) A robust and flexible job scheduling system that ensures reliable execution order, detects cycles, and provides real-time scheduling feedback. This project demonstrates efficient job management and scheduling techniques suitable for various applications requiring dependency resolution and execution order management.
