Семинар 6: структуры данных с подтверждением
    
Реализовать одну из следующих структур данных с подтверждением. 

Один человек делает доклад про: 
1) мотивацию, с которой схема была разработана 
2) поддерживаемые структурой операции и их асимптотики
3) реализации и конкретные цифры производительностей операций  
4) как делается тестирование программ

Второй человек пишет программу, которая тестирует программу третьего члена команды.

Третий член команды реализуют структуру, на разных языках программирования, и интерфейс для тестирования (скажем, вывод результатов в файл). 

Реализовывать на другом языке, чем в другой группе или в 2018 году!
1) [Improving Authenticated Dynamic Dictionaries, with Applications to Cryptocurrencies](https://eprint.iacr.org/2016/994), реализовать структуру данных без склеивания доказательств (batch proving / verification), готовую реализацию можно на языке Scala можно найти на https://github.com/input-output-hk/scrypto, поэтому решение на Scala не допускается!
2) [Edrax: A Cryptocurrency with Stateless Transaction Validation](https://eprint.iacr.org/eprint-bin/getfile.pl?entry=2018/968&version=20181014:152509&file=968.pdf). Реализовать лишь схему с разрежённым деревом Мёркла!
3) [Efficient Asynchronous Accumulators for Distributed PKI](https://eprint.iacr.org/2015/718.pdf). (см. секцию 4 для реализации) 
4) [Efficient Authenticated Dictionaries with Skip Lists and Commutative Hashing](https://www.cs.jhu.edu/~goodrich/cgc/pubs/hashskip.pdf). 
5) [Super-efficient Aggregating History-independent Persistent Authenticated Dictionaries](http://tamperevident.cs.rice.edu/papers/paper-pad.pdf). Реализовать схему Tuple-based PADs (секция 4 статьи).
6) [Compact Sparse Merkle Trees](https://eprint.iacr.org/2018/955.pdf). Решение на Python не допускается!
7) [Trading Accumulation Size for Witness Size: A Merkle TreeBased Universal Accumulator via Subset Differences](https://eprint.iacr.org/2019/1186.pdf).
8) [Utreexo: A dynamic hash-based accumulatoroptimized for the Bitcoin UTXO set](https://eprint.iacr.org/2019/611.pdf).
9) [Fash hash-based additive accumulators](https://github.com/bigspider/accumulator/blob/master/docs/paper-draft.pdf). Решение на Python не допускается! 

Реализации:
1) https://github.com/GaDiss/ctypto2020-task6
2)
3) https://github.com/naumovdk/Yakubov-Merkle-tree
4) https://github.com/alexanderlvarfolomeev/AuthenticatedDictionary
5)
6) https://github.com/gleb-kov/csmt
7) https://github.com/18756/CryptoProject
8) https://github.com/DRomanenko/utreexo
9) https://github.com/IvanMaslov/AdditiveAccumulator