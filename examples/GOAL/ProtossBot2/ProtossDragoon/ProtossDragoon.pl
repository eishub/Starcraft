:- dynamic
position/2,
attack/2,
unit/2,
base/4,
enemy/7,
id/1,
friendly/6,
chokepoint/2,
attack/1,
moving/0.


enemyBase(X, Y) :- base(X, Y, 'true', _), 
friendly(_, "Protoss Nexus", _,_, X1, Y1), distance(X, Y, X1, Y1, Res), Res > 10.

rallyLocation(X,Y, RX,RY) 
	:-	findall([D,BX,BY], (chokepoint(BX,BY), distance(X,Y,BX,BY,D)), L),
		sort(L, [[_,RX,RY]|_]). 
		
distance(X1,Y1,X2,Y2,D) :- D is sqrt((X2-X1)**2 + (Y2-Y1)**2).	 

closeEnemy(X,Y) :- position(X1,Y1), enemy(_,_,_,_,_,X2,Y2), distance(X1, Y1, X2, Y2, D), D < 2, X is X1 + 3, Y is Y1 + 3.