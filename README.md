# SelfGraph
生成图代码，可由邻接矩阵直接生成图，也可通过逐渐添加节点的方式生成图。同时实现了添加、删除节点、深度遍历、广度遍历、最短路径等方法。

根据邻接矩阵（方阵）创建带有默认名的图,n为节点的个数

public SelfGraph createDirectedGraph(int n, double[][] Matrix)

根据邻接矩阵（方阵）创建带有名字的图,names为每一个节点的名字

public SelfGraph createDirectedGraph(int n, double[][] Matrix, List<String> names)

添加默认名字或指定名字的节点
  
public boolean addNewNode()
  
public boolean addNewNode(String name)
  
根据节点添加边，并且指定边的长度
  
public boolean addNextEdge(Node startNode, Node endNode, double len)
  
public boolean addNextEdge(String startName, String endName, double len)
  
public boolean addNextEdge(String startName ,List<String> endNames,List<Double> lengths)
  
public boolean addNextEdge(Node startNode, List<Node> endNodes, List<Double> lengths)
  
public boolean addPreEdge(Node endNode, Node startNode, double len)
  
public boolean addPreEdge(String endName, String startName, double len)
  
public boolean addPreEdge(String endName ,List<String> startNames,List<Double> lengths)
  
public boolean addPreEdge(Node endNode, List<Node> startNodes, List<Double> lengths)

删除节点
  
public boolean delete(Node node)
  
public boolean delete(String name)

根据名字获取节点的引用
  
public Node get(String name)

获取两个节点边的长度
  
public double get2NodesLength(String startName, String endName)
  
public double get2NodesLength(String startName, String endName)
