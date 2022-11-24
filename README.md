# BlockchainSeries

This is a brief series of four mini projects that illustrate my understanding of blockchain technology! Each part of the code is heavily commented and was done for my Distributed Systems 1 class (CSC 435) at DePaul University. Each of these projects build on one another and are mostly done for the purpose of showing I know what I'm doing; like a Proof-of-Concept! See my portfolio entry at http://wrightdj99.github.io for more exposition on the topic. 

For now, all you have to do to see this is just download the repository and run each Java file in the terminal. The assignment was meant to be very simple in its running so that nobody would have to do any legwork with sifting through .idea or other IDE-specific files. I hope you enjoy this!

----------------------------
CODING STYLE OBSERVATION:
----------------------------

One thing that I noticed after the fact (i.e. after the assignment was turned in and discussed in class) is that my design for these mini projects is a bit more intuitive than those of others in the class. Not to sound arrogant or pat myself on the back, but for this assignment a lot of people in the class would just jam all their code into one or two "God methods" without any consideration for SOLID practices or DRY code. Even the bit of starter code that our professor gave us (Dr. Clark Elliott) seemed to be just slammed into a couple of huge methods at certain points.

For my code, I would rather have a bunch of different helper methods that feed into a Main method or else a "doit()"/"doTheThing()"/coordinator method in a class. This way, you are less likely to repeat code, and your code will be world's easier to read and understand by someone other than you. 
