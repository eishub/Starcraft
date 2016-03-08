enemyBase(X, Y) :- percept(base(X, Y, 'true', _)), 
percept(friendly(_, "Terran Command Center", _, _, _, _, _, X1, Y1)), distance(X, Y, X1, Y1, Res), Res > 10.
		
closestEnemy(RX,RY) 
	:-	findall([D,BX,BY], (percept(enemy(_, Id,_,_, 'false' ,_, _, BX, BY)), percept(position(MX, MY)), distance(X,Y,MX,MY,D)), L),
		sort(L, [[_,RX,RY]|_]).
		
distance(X1,Y1,X2,Y2,Res):- Res is sqrt((X2-X1)**2 + (Y2-Y1)**2). 