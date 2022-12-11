# Teseo e il Minotauro

## Descrizione
In questo gioco l'obiettivo di Teseo è quello di raggiungere l'uscita del labirinto senza incontrare sul proprio cammino il Minotauro.

In base alla modalità scelta, l'IA di Teseo, del Minotauro o di entrambi, proverà a vincere la partita o quantomeno a disputarla in maniera ottimale, all'interno di un labirinto generato casualmente dal computer.

## Come funziona
Il seguente applicativo è da intendersi come un semplice esercizio sull'utilizzo del **backtracking**, una tecnica che permette di risolvere problemi esplorando tutte le possibili combinazioni e scartando quelle che non soddisfano i vincoli prestabiliti, e della **ricorsione**, ovvero una funzione che richiama sé stessa fino al raggiungimento di una condizione di terminazione.

### IA di Teseo e il Minotauro
L'algoritmo utilizzato per implementare l'IA di Teseo e il Minotauro esplora il labirinto fino al raggiungimento del punto di arrivo e restituisce il percorso migliore.

Ogni cella percorribile ne ha almeno un'altra adiacente, posta su una delle quattro direzioni possibili (destra, sinistra, sopra sotto), una volta individuata viene marchiata e il metodo viene invocato nuovamente per quella successiva. 
Quando la condizione di terminazione viene soddisfatta, che in questo caso corrisponde al raggiungimento delle coordinate della destinazione impostata come parametro, avremo a disposizione la lista di tutti percorsi possibili, tra i quali successivamente verrà selezionato quello più corto.

### Generazione dei labirinti
Per quanto concerne la generazione dei labirinti è importante fare un distinguo sulla tipologia di questi.
Dati due punti A e B, in un labirinto **perfetto** esisterà sempre e solo un unico percorso, viceversa potrebbero essercene molteplici nel caso di un **labirinto non perfetto**.

Per generare un labirinto perfetto è stato utilizzato un algoritmo che, a partire da una cella, sceglie casualmente un muro adiacente presente in una delle direzioni possibili e, se la cella da raggiungere non è stata visitata, crea un passaggio "abbattendo" il muro, collegando così le due celle.
La cella raggiunta diventa il nuovo punto iniziale e le istruzioni sin qui elencate si ripetono.
Nel caso in cui fossero state visitate tutte le celle adiacenti, l'algoritmo ritorna all'ultima cella che presenta dei muri finché questa non coincide con quella presente alle coordinate specificate per il punto di partenza.

Partendo da un labirinto perfetto, è possibile generarne uno non perfetto rimuovendo le cosiddette dead-ends, ovvero quelle celle che presentano tre muri.
Per farlo, è sufficiente iterare sulla matrice rappresentativa del labirinto, individuare le celle con questa caratteristica e abbattere i muri adiacenti.

