CREATE (c1:Client { name:'Client 1' });

CREATE (f1:Fund { name:'Fund 1' });

MATCH (c1:Client { name:'Client 1' }),(f1:Fund { name:'Fund 1' }) MERGE (c1)-[r:MANAGE]->(f1);

CREATE (p1:Portfolio { name:'Portfolio 1', currency:'USD' });

MATCH (f1:Fund { name:'Fund 1' }),(p1:Portfolio { name:'Portfolio 1' }) MERGE (f1)-[r:IS_COMPOSED_OF]->(p1);

CREATE (p2:Portfolio { name:'Portfolio 2', currency:'USD' });

MATCH (f1:Fund { name:'Fund 1' }),(p2:Portfolio { name:'Portfolio 2' }) MERGE (f1)-[r:IS_COMPOSED_OF]->(p2);


CREATE (e:Exposure { key:'ZBU16', type:'T-Bond' });

MATCH (e:Exposure { key:'ZBU16' }),(p1:Portfolio { name:'Portfolio 1' }) MERGE (p1)-[r:IS_COMPOSED_OF]->(e);


CREATE (a:Asset { key:'30Y-UST', type:'Bond' });

MATCH (a:Asset { key:'30Y-UST' }),(p2:Portfolio { name:'Portfolio 2' }) MERGE (p2)-[r:IS_COMPOSED_OF]->(a);


CREATE (agreement:Agreement { key:'A1', type:'CSA' });
CREATE (rule:Rule { key:'r1', type:'Haircut', value:0.05 });
CREATE (category:Category { key:'Cat1', type:'G-Bond' });


MATCH (agreement:Agreement { key:'A1' }),(p2:Portfolio { name:'Portfolio 2' }) MERGE (p2)-[r:IS_UNDER]->(agreement);


MATCH (agreement:Agreement { key:'A1' }),(rule:Rule { key:'r1' }) MERGE (agreement)-[r:HAS]->(rule);

MATCH (category:Category { key:'Cat1' }),(rule:Rule { key:'r1' }) MERGE (rule)-[r:APPLY_TO]->(category);

MATCH (a:Asset { key:'30Y-UST' }),(category:Category { key:'Cat1' }) MERGE (a)-[r:IS_PART_OF]->(category);