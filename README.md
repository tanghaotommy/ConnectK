# ConnectK
AI to ConnectK

This is a course project developed with Jiawei Gu.
Written AI for ConnectK based on the existing basic codes developed by course assistants.

1. Implemented minimax tree for searching the next best action (place to put the chess).
2. Implemented alpha beta pruning for improving search effiency.
3. Used priority queue for storing next state in order to optimize better alpha-beta.
4. Implemented scoring function as follows: 
For example
when k = 5
for 1 consecutive positions, score += 1 
for 2 consecutive positions, score += 10 
for 3 consecutive positions, score += 100 
for 4 consecutive positions, score += 1000 
Also, considered empty between possible connectK in a row(column)
