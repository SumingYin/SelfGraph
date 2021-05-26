package com.ysm.offer;

import org.junit.Test;

import java.util.*;

/**
 * 实现图的数据结构
 * 可赋予每个节点String名字
 */
@SuppressWarnings("all")
public class SelfGraph {

    // 哈希表存储所有节点
    // 为什么以这样的方式存储图?因为涉及到删除、添加的操作，
    // 如果以索引的方式将会变得非常麻烦
    HashMap<String, Node> graphNodesMap = null;

    // 代表真实节点的个数
    int nodeNums = 0;
    // 代表默认情况下，赋予节点的名字
    int defaultName = 0;
    // 哈希表存储节点name与idx之间的关系
    // 不应该存在name相同的节点


    // 为了方便获取节点的引用和名字
    HashMap<Node, String> nodeHashName = null;

    // 添加字段，保存重新排过序后的节点索引
    List<Node> topologicalSortList = null;


    // 初始化工作
    public SelfGraph() {
        graphNodesMap = new HashMap<>();
        nodeHashName = new HashMap<>();
    }

    public static void main(String[] args) {
        SelfGraph selfGraph = new SelfGraph();
        int nodeNum = 5;
        double[][] nodes = new double[nodeNum][nodeNum];
        Random random = new Random();
        random.setSeed(1);
        for (int i = 0; i < nodeNum; i++) {
            for (int j = 0; j < nodeNum; j++) {
                int num = random.nextInt(10);
                if (num > 0) {
                    if (i == j && num > 0) {
                        continue;
                    }
                    nodes[i][j] = num;
                }
            }
        }
        // 1.邻接矩阵的方式
        selfGraph.createDirectedGraph(nodeNum, nodes);
        System.out.println(selfGraph);

        /*
        根据名字查到两者节点的索引，这两个节点之间的长度是多少
        根据index查到两个节点引用，这两个几点之间的长度是多少
         */

        Node node1 = selfGraph.get("1");
        Node node2 = selfGraph.get("2");
        double node2len = selfGraph.get2NodesLength(node1, node2);
        System.out.println(node2len);

        // 2.逐个加入节点的方式，可在邻接矩阵的基础上，进行添加或删除节点、添加边
        SelfGraph selfGraph1 = new SelfGraph();
        for (int i = 0; i < 10; i++) {
            selfGraph1.addNewNode("" + i);
        }

        // 添加边
        // 1.根据名称添加边
        selfGraph1.addNextEdge("1", "2", 10);
        selfGraph1.addNextEdge("3", "2", 8);
        selfGraph1.addNextEdge("3", "4", 3);
        selfGraph1.addNextEdge("5", "6", 1);
        selfGraph1.addNextEdge("7", "9", 5);

        // 2.根据名称数组添加边
        List<String> stringList = new ArrayList<>();
        ArrayList<Double> doubles = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            stringList.add(i + "");
            doubles.add((double) i);
        }

        selfGraph1.addNextEdge("8", stringList, doubles);
        System.out.println(selfGraph1);


    }

    @Test
    public void test(){
        // 测试最短路径算法
        SelfGraph selfGraph = new SelfGraph();
        double [][] Matrix = new double[6][6];
        Matrix[0][1] = 5;
        Matrix[0][2] = 3;
        Matrix[1][2] = 2;
        Matrix[1][3] = 6;
        Matrix[2][3] = 7;
        Matrix[2][4] = 4;
        Matrix[2][5] = 10;
        Matrix[3][4] = 1;
        Matrix[3][5] = 1;
        Matrix[4][5] = 2;
        Matrix[5][1] = 3;
        // 创建图
        selfGraph.createDirectedGraph(6,Matrix);

        // 测试应为无环
        System.out.println(selfGraph.graphHasCircle());

        // 测试最短路径
        //System.out.println(selfGraph.shortestPath2Nodes("2","5",selfGraph.graphHasCircle()));

        System.out.println(selfGraph);

    }


    @Test
    public void test1(){
        // 拓扑排序测试
        SelfGraph selfGraph = new SelfGraph();
        // 使用默认的0、1、2、3、4、5来添加节点
        selfGraph.addNewNode();
        selfGraph.addNewNode();
        selfGraph.addNewNode();
        selfGraph.addNewNode();
        selfGraph.addNewNode();
        selfGraph.addNewNode();

        // 添加边
        selfGraph.addNextEdge("5","0");
        selfGraph.addNextEdge("5","2");
        selfGraph.addNextEdge("4","0");
        selfGraph.addNextEdge("4","1");
        selfGraph.addNextEdge("2","3");
        selfGraph.addNextEdge("3","1");

        // 拓扑排序
        List<String> stringList = selfGraph.topologicalSortNames();
        // 打印出排序后的字符，查看是否正确
        for (String s :
                stringList) {
            System.out.println(s);
        }
        System.out.println("================================================");
        // 拓扑排序较复杂的情况
        SelfGraph selfGraph1 = new SelfGraph();
        for (int i = 0; i < 8; i++) {
            selfGraph1.addNewNode();
        }

        // 添加边
        selfGraph1.addNextEdge("0","1");
        selfGraph1.addNextEdge("0","2");
        selfGraph1.addNextEdge("0","4");
        selfGraph1.addNextEdge("2","4");
        selfGraph1.addNextEdge("2","5");
        selfGraph1.addNextEdge("2","6");
        selfGraph1.addNextEdge("4","6");
        selfGraph1.addNextEdge("6","7");
        selfGraph1.addNextEdge("3","7");
        selfGraph1.addNextEdge("4","7");
        selfGraph1.addNextEdge("5","7");
        selfGraph1.addNextEdge("0","1");

        List<String> strings = selfGraph1.topologicalSortNames();
        for (String s :
                strings) {
            System.out.println(s);
        }
    }

    /**
     * 函数重载
     *
     * @param n      创建图节点的个数
     * @param Matrix 邻接矩阵的方式创建图，给每个节点起名字，默认情况下为0、1、2、n-1
     * @return 返回一个图对象
     */
    public SelfGraph createDirectedGraph(int n, double[][] Matrix) {
        // 节点的个数
        nodeNums = n;
        // 长度不为n，或长度为0，长度不相等，均返回null
        if (Matrix == null || Matrix.length != n) {
            return null;
        } else if (Matrix[0] == null || Matrix.length != Matrix[0].length) {
            return null;
        }

        Set<String> set = new HashSet<String>();
        // 首先将所有节点放到GraphNodesMap中
        // 此时已经开辟尽管返回null，但已经开辟了空间，可以先确定无误
        // 再进行开辟空间
        for (int i = 0; i < n; i++) {
            // 新创建一个节点，并且为每个节点进行赋予名字
            String name = "" + i;
            Node node = new Node(name);
            graphNodesMap.put(name, node);
            nodeHashName.put(node, name);
            defaultName++;
            set.add(name);
        }
        if (set.size() != n) {
            System.out.println("存在重复的元素");
            defaultName = 0;
            nodeNums = 0;
            graphNodesMap = new HashMap<>();
            nodeHashName = new HashMap<>();
            return null;
        }

        // 根据矩阵的值进行初始化图
        for (int i = 0; i < n; i++) {
            String name = "" + i;
            Node startNode = graphNodesMap.get(name);
            for (int j = 0; j < n; j++) {
                double length = Matrix[i][j];
                if (length > 0) {
                    String name1 = "" + j;
                    Node endNode = graphNodesMap.get(name1);
                    Edge edge = new Edge(startNode, endNode, length);
                    // 边集合
                    startNode.edgesMap.put(endNode, edge);
                    // 后继节点与前驱节点
                    startNode.nextNodeList.add(endNode);
                    endNode.preNodeList.add(startNode);
                } else {
                    continue;
                }

            }
        }

        return this;


    }

    public SelfGraph createDirectedGraph(int n, double[][] Matrix, List<String> names) {


        nodeNums = n;
        // 长度不为n，或长度为0，长度不相等，均返回null，命名的长度与n不相等
        if (Matrix == null || Matrix.length != n) {
            return null;
        } else if (Matrix[0] == null || Matrix.length != Matrix[0].length) {
            return null;
        } else if (names == null || names.size() != n) {
            return null;
        }

        Set<String> set = new HashSet<String>();
        // 首先将所有节点放到GraphNodesList中
        for (int i = 0; i < n; i++) {
            String name = names.get(i);
            // 新创建一个节点，并且为每个节点进行赋值索引
            if (name == null || name.length() == 0) {
                return null;
            }
            Node node = new Node(name);
            graphNodesMap.put(name, node);
            nodeHashName.put(node, name);
            set.add(name);
        }
        if (set.size() != n) {
            System.out.println("存在重复的元素");
            defaultName = 0;
            nodeNums = 0;
            graphNodesMap = new HashMap<>();
            nodeHashName = new HashMap<>();
            return null;
        }

        // 根据矩阵的值进行初始化图
        for (int i = 0; i < n; i++) {
            String name = names.get(i);
            Node startNode = graphNodesMap.get(name);
            for (int j = 0; j < n; j++) {
                double length = Matrix[i][j];
                if (length > 0) {
                    String name1 = names.get(i);
                    Node endNode = graphNodesMap.get(name1);
                    Edge edge = new Edge(startNode, endNode, length);
                    // 边集合
                    startNode.edgesMap.put(endNode, edge);
                    // 后继节点与前驱节点
                    startNode.nextNodeList.add(endNode);
                    endNode.preNodeList.add(startNode);
                } else {
                    continue;
                }

            }
        }


        return this;
    }


    /**
     * @return
     */
    public boolean addNewNode() {
        // 如果不指定添加的名字，则以nodeNums递增的方式添加
        // 不考虑其他
        return addNewNode(defaultName + "");
    }

    public boolean addNewNode(String name) {

        if (name == null || name.length() == 0 || graphNodesMap.containsKey(name)) {
            return false;
        }
        defaultName++;
        nodeNums++;
        Node node1 = new Node(name);
        graphNodesMap.put(name, node1);
        nodeHashName.put(node1,name);
        return true;
    }

    /**
     * @param startNode 初始节点
     * @param endNode   末尾节点
     * @param len       边的长度
     * @return
     */
    public boolean addNextEdge(Node startNode, Node endNode, double len) {
        if (startNode == null || endNode == null || !graphNodesMap.containsValue(startNode) || !graphNodesMap.containsValue(endNode) || len <= 0) {
            return false;
        }
        List<Node> list = new ArrayList<>();
        List<Double> list1 = new ArrayList<>();
        list.add(endNode);
        list1.add(len);
        return addNextEdge(startNode, list, list1);
    }
    // 默认添加的长度是1
    public boolean addNextEdge(String startName,String endName){
        return addNextEdge(startName,endName,1);
    }

    public boolean addNextEdge(Node startNode,Node endNode){
        return addNextEdge(startNode,endNode,1);
    }


    // 同过给定名字的方式添加节点
    public boolean addNextEdge(String startName, String endName, double len) {
        if (endName == null || startName == null || startName.length() == 0 || endName.length() == 0 || len <= 0) {
            return false;
        }
        return addNextEdge(graphNodesMap.get(startName), graphNodesMap.get(endName), len);
    }

    public boolean addNextEdge(String startName, List<String> endNames, List<Double> lengths) {
        if (startName == null || endNames == null || lengths == null) {
            return false;
        } else if (!graphNodesMap.containsKey(startName)) {
            return false;
        }
        int size = endNames.size();
        int lenSize = lengths.size();
        if (size != lenSize) {
            return false;
        }

        // 需要先检查所有添加的边是否符合条件
        for (int i = 0; i < size; i++) {
            String endName = endNames.get(i);
            Double aDouble = lengths.get(i);
            if (endName != null && graphNodesMap.containsKey(endName) && aDouble != null && aDouble > 0) {
                continue;
            } else {
                return false;
            }
        }

        // 全部符合条件后添加
        Node startNode = graphNodesMap.get(startName);
        for (int i = 0; i < size; i++) {
            Node endNode = graphNodesMap.get(endNames.get(i));
            Double aDouble = lengths.get(i);
            Edge edge = new Edge(startNode, endNode, aDouble);
            if (startNode.edgesMap.containsKey(endNode)) {
                continue;
            } else {
                // 必须呈三组出现
                startNode.nextNodeList.add(endNode);
                endNode.preNodeList.add(startNode);
                startNode.edgesMap.put(endNode, edge);
            }

        }
        return true;

    }

    public boolean addNextEdge(Node startNode, List<Node> endNodes, List<Double> lengths) {

        // 如果图中不含有startNode，或传入的值为null，则返回false
        if (!graphNodesMap.containsValue(startNode) || startNode == null || endNodes == null || lengths == null) {
            return false;
        }
        int size = endNodes.size();
        int lenSize = lengths.size();
        if (size != lenSize) {
            return false;
        }
        // 需要先检查所有添加的边是否符合条件
        for (int i = 0; i < size; i++) {
            Node endNode = endNodes.get(i);
            Double aDouble = lengths.get(i);
            if (endNode != null && graphNodesMap.containsValue(endNode) && aDouble != null && aDouble > 0) {
                continue;
            } else {
                return false;
            }
        }
        // 全部符合条件后添加
        for (int i = 0; i < size; i++) {
            Node endNode = endNodes.get(i);
            Double aDouble = lengths.get(i);
            Edge edge = new Edge(startNode, endNode, aDouble);

            if (startNode.edgesMap.containsKey(endNode)) {
                continue;
            } else {
                // 必须呈三组出现
                startNode.nextNodeList.add(endNode);
                endNode.preNodeList.add(startNode);
                startNode.edgesMap.put(endNode, edge);
            }

        }
        return true;

    }

    /**
     * @param endNode  末尾节点
     * @param preNodes 前驱节点
     * @param lengths  添加长度
     * @return
     */
    public boolean addPreEdge(Node endNode, List<Node> preNodes, List<Double> lengths) {
        // 如果图中不含有startNode，或传入的值为null，则返回false
        if (!graphNodesMap.containsValue(endNode) || endNode == null || preNodes == null || lengths == null) {
            return false;
        }
        int size = preNodes.size();
        // 需要先检查所有添加的边是否符合条件
        for (int i = 0; i < size; i++) {
            Node startNode = preNodes.get(i);
            Double aDouble = lengths.get(i);
            if (startNode != null && graphNodesMap.containsValue(startNode) && aDouble != null && aDouble > 0) {
                continue;
            } else {
                return false;
            }
        }
        // 全部符合条件后添加
        for (int i = 0; i < size; i++) {
            Node startNode = preNodes.get(i);
            Double aDouble = lengths.get(i);
            Edge edge = new Edge(startNode, endNode, aDouble);
            // 必须呈三组出现
            if (startNode.edgesMap.containsKey(endNode)) {
                continue;
            } else {
                // 必须呈三组出现
                startNode.nextNodeList.add(endNode);
                endNode.preNodeList.add(startNode);
                startNode.edgesMap.put(endNode, edge);
            }

        }
        return true;
    }

    public boolean addPreEdge(Node endNode, Node startNode, double len) {

        if (startNode == null || endNode == null || !graphNodesMap.containsValue(startNode) || !graphNodesMap.containsValue(endNode) || len <= 0) {
            return false;
        }
        List<Node> list = new ArrayList<>();
        List<Double> list1 = new ArrayList<>();
        list.add(startNode);
        list1.add(len);
        return addNextEdge(endNode, list, list1);

    }

    public boolean addPreEdge(String endName, List<String> startNames, List<Double> lengths) {
        if (endName == null || startNames == null || lengths == null) {
            return false;
        } else if (!graphNodesMap.containsKey(endName)) {
            return false;
        }
        int size = startNames.size();
        int lenSize = lengths.size();
        if (size != lenSize) {
            return false;
        }

        // 需要先检查所有添加的边是否符合条件
        for (int i = 0; i < size; i++) {
            String startName = startNames.get(i);
            Double aDouble = lengths.get(i);
            if (endName != null && graphNodesMap.containsKey(startName) && aDouble != null && aDouble > 0) {
                continue;
            } else {
                return false;
            }
        }

        // 全部符合条件后添加
        Node endNode = graphNodesMap.get(endName);
        for (int i = 0; i < size; i++) {
            Node startNode = graphNodesMap.get(startNames.get(i));
            Double aDouble = lengths.get(i);
            Edge edge = new Edge(startNode, endNode, aDouble);
            if (startNode.edgesMap.containsKey(endNode)) {
                continue;
            } else {
                // 必须呈三组出现
                startNode.nextNodeList.add(endNode);
                endNode.preNodeList.add(startNode);
                startNode.edgesMap.put(endNode, edge);
            }

        }
        return true;

    }

    public boolean addPreEdge(String endName, String startName, double len) {
        if (endName == null || startName == null || startName.length() == 0 || endName.length() == 0 || len <= 0) {
            return false;
        }
        return addPreEdge(graphNodesMap.get(endName), graphNodesMap.get(startName), len);
    }

    // 默认添加的长度是1
    public boolean addPreEdge(String endName,String startName){
        return addPreEdge(endName,startName,1);
    }

    // 默认添加的长度是1
    public boolean addPreEdge(Node endNode,Node startNode){
        return addPreEdge(endNode,startNode,1);
    }


    /**
     * @param node 删除节点
     * @return
     */
    public boolean delete(Node node) {

        // 判定该节点是否在graph中,如果不在，则直接返回
        if (!graphNodesMap.containsValue(node) || node == null) {
            return true;
        }

        List<Node> nextNodeList = node.nextNodeList;
        for (Node nextNode :
                nextNodeList) {
            nextNode.preNodeList.remove(node);
        }

        List<Node> preNodeList = node.preNodeList;
        for (Node preNode :
                preNodeList) {
            // 前驱节点不仅要删除该后继节点，同时边也要删除
            preNode.nextNodeList.remove(node);
            preNode.edgesMap.remove(node);
        }

        // 最后从图中删除，整体节点数减少
        String s = nodeHashName.get(node);
        nodeHashName.remove(node);
        graphNodesMap.remove(s);
        nodeNums--;
        return true;
    }

    // 根据名字进行删除
    public boolean delete(String name) {
        // 删除其所有与之相关节点之间的联系
        if (name == null || name.length() == 0 || !nodeHashName.containsValue(name)) {
            return false;
        }

        return delete(graphNodesMap.get(name));
    }


    // 根据索引获取node
    public Node get(String name) {
        return graphNodesMap.get(name);
    }

    // 根据Node获取名字
    public String get(Node node) {
        return nodeHashName.get(node);
    }

    // 根据传入的两个节点名字的获取长度
    // 根据传入的两个节点的引用获取长度
    public double get2NodesLength(String startName, String endName) {
        if (startName == null || endName == null || !graphNodesMap.containsKey(startName) || !graphNodesMap.containsKey(endName)) {
            return -1.0;
        }
        return get2NodesLength(graphNodesMap.get(startName), graphNodesMap.get(endName));
    }

    public double get2NodesLength(Node startNode, Node endNode) {

        if (startNode == null || endNode == null || !graphNodesMap.containsValue(startNode) || !graphNodesMap.containsValue(endNode)) {
            // 返回负数，说明出错
            return -1.0;
        }

        HashMap<Node, Edge> edgesMap = startNode.edgesMap;
        Edge edge = edgesMap.get(endNode);
        if (edge == null) {
            // 说明两者之间不存在边
            return 0;
        }
        return edge.length;

    }

    // 根据名字进行深度遍历
    public boolean depthFirstSearch(SelfGraph selfGraph, String name) {
        if (name == null || name.length() == 0 || !selfGraph.graphNodesMap.containsKey(name)) {
            return false;
        }

        return depthFirstSearch(selfGraph, selfGraph.graphNodesMap.get(name));
    }

    public boolean depthFirstSearch(String name){
        return depthFirstSearch(this,name);
    }

    // 重新设置所有节点是否被访问
    public boolean reSetIsVisited(SelfGraph selfGraph) {
        if (selfGraph == null || selfGraph.graphNodesMap == null) {
            return false;
        }
        // 通过迭代器，首先将所有节点是否被访问过设置为false
        Iterator<String> iterator = graphNodesMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            Node node1 = graphNodesMap.get(next);
            node1.isVisited = false;
        }
        return true;
    }

    public boolean reSetIsVisited(){
        return reSetIsVisited(this);
    }



    /**
     * 深度遍历，递归进行遍历
     * 在深度遍历之前应该reSetIsVisited
     *
     * @param selfGraph
     * @param node
     * @return
     */
    public boolean depthFirstSearch(SelfGraph selfGraph,Node node) {

        HashMap<String, Node> graphNodesMap = selfGraph.graphNodesMap;
        if (selfGraph == null || graphNodesMap == null || !graphNodesMap.containsValue(node)) {
            return false;
        }

        // 从node开始进行深度遍历
        node.isVisited = true;
        List<Node> nextNodeList = node.nextNodeList;
        if(nextNodeList == null){
            return true;
        }else {
            for (Node n :
                    nextNodeList) {
                if(!n.isVisited){
                    depthFirstSearch(selfGraph,n);
                }
            }
        }


        return true;
    }

    public boolean depthFirstSearch(Node node){
        return depthFirstSearch(this,node);
    }
    // 广度遍历，借助队列来实现
    public boolean breadthFirstSearch(SelfGraph selfGraph, Node node) {
        return true;
    }



    /**
     * 判断该图中是否存在以node为起点和终点的环
     *
     * @param startEndNode
     * @return
     */
    public boolean nodeHasCircle(SelfGraph selfGraph,Node startEndNode) {
        if(selfGraph == null){
            return false;
        }
        HashMap<String, Node> graphNodesMap = selfGraph.graphNodesMap;
        if( startEndNode == null || graphNodesMap == null || !graphNodesMap.containsValue(startEndNode)){
            return false;
        }
        // 重新设置是否访问
        reSetIsVisited(selfGraph);
        // 深度优先遍历
        depthFirstSearch(selfGraph,startEndNode);
        // 检查该节点的前驱节点是否存在isVisited为true的节点
        // 如果存在，则一定存在
        List<Node> preNodeList = startEndNode.preNodeList;
        for (Node n :
                preNodeList) {
            if (n.isVisited){
                return true;
            }
        }

        return false;
    }

    public boolean nodeHasCircle(Node startEndNode){
        return nodeHasCircle(this,startEndNode);
    }

    /**
     * 判断该图是否存在环
     * @param selfGraph
     * @return
     */
    public boolean graphHasCircle(SelfGraph selfGraph){

        if(selfGraph == null){
            return false;
        }

        HashMap<String, Node> graphNodesMap = selfGraph.graphNodesMap;
        if(graphNodesMap == null || graphNodesMap.isEmpty()){
            return false;
        }
        Set<String> strings = graphNodesMap.keySet();
        for (String s:
             strings) {
            // 逐个节点判断，如果其中一个节点存在环，则该图存在环
            if(nodeHasCircle(selfGraph,graphNodesMap.get(s))){
                return true;
            }
        }

        return false;

    }
    public boolean graphHasCircle(){
        return graphHasCircle(this);
    }

    // 返回两节点之间的最短距离,有向无环图
    public double shortestPath2Nodes(String startName, String endName,boolean graphHasCircle) {

        if (startName == null || endName == null || startName.length() == 0 || endName.length() == 0 || !graphNodesMap.containsKey(startName) || !graphNodesMap.containsKey(endName)) {
            return -1.0;
        }

        return shortestPath2Nodes(graphNodesMap.get(startName), graphNodesMap.get(endName),graphHasCircle);
    }

    // 求解最短路径的时候涉及到浮点数的比较
    final double error = 1e-6;

    /**
     * 先不考虑使用教科书的dijiska算法，之后再试一试
     * 最直观采用动态规划的方法来计算两个点之间的最短距离
     * 显然min(startNode,endNode) = min(len(startNode,nextNode) + min(nextNode,endNode)) 对任意的nextNode
     * 由上式可得：可采用递归的方式来进行，期间需要添加一些判断语句
     * 但该算法遇到存在环的有向图时会出现问题，所以需要先判断是否有环，其中graphHasCircle必须为false
     * @param startNode
     * @param endNode
     * @return
     */
    public double shortestPath2Nodes(Node startNode, Node endNode,boolean graphHasCircle) {
        // 两者指向相同则返回0
        if (endNode == startNode) {
            return 0.0;
        }
        List<Node> nextNodeList = startNode.nextNodeList;
        // 无法到达最终节点时，返回-1.0
        if (nextNodeList == null || nextNodeList.size() == 0 || graphHasCircle) {
            return -1.0;
        } else {

            List<Double> minList  = new ArrayList<>();
            for (Node nextNode :
                    nextNodeList) {
                double sWithn = get2NodesLength(startNode, nextNode);
                if(nextNode == endNode){
                    minList.add(sWithn);
                    continue;
                }
                double nextMin = shortestPath2Nodes(nextNode,endNode,graphHasCircle);
                if(Math.abs(nextMin+1.0) < error){
                    // 说明nextNodeList为null无法到达endNode
                    // 此时无需将该值加入进行比较
                    continue;
                }else {
                    minList.add(sWithn + nextMin);
                }

            }
            // 返回最小值，无法到达endNode
            if(minList.size() == 0){
                return -1.0;
            }
            return min(minList);

        }

    }


    public boolean topologicalSort(SelfGraph selfGraph){
        // 返回false的情况
        if (selfGraph == null) {
            return false;
        }
        HashMap<String, Node> graphNodesMap = selfGraph.graphNodesMap;
        // 当含有环的时候返回false
        if(graphNodesMap == null || graphNodesMap.isEmpty() || selfGraph.graphHasCircle()){
            return false;
        }

        selfGraph.topologicalSortList = new ArrayList<>();
        // 充当队列的作用
        List<Node> queue = new ArrayList<>();

        // 根据当前图的所有节点制作入库表
        Set<String> strings = graphNodesMap.keySet();
        Iterator<String> iterator = strings.iterator();
        // 记录当前节点的前驱节点的个数
        while (iterator.hasNext()) {
            Object next =  iterator.next();
            Node node = graphNodesMap.get(next);
            List<Node> preNodeList = node.preNodeList;
            if(preNodeList == null || preNodeList.size() == 0){
                node.preNodeNums = 0;
                // 该节点的索引入栈
                queue.add(node);
            }else {
                node.preNodeNums = preNodeList.size();
            }
        }
        // 针对当前栈中前驱已经为0的节点进行操作
        while(queue.size()!=0){

            // 出队，并将它存入到selfGraph的topologicalSortList字段中
            Node node = queue.get(0);
            queue.remove(0);
            selfGraph.topologicalSortList.add(node);

            List<Node> nextNodeList = node.nextNodeList;
            if(nextNodeList == null || nextNodeList.size() == 0){
                continue;
            }else {

                for (Node nextNode :
                        nextNodeList) {
                    nextNode.preNodeNums--;
                    if(nextNode.preNodeNums == 0){
                        // 进队
                        queue.add(nextNode);
                    }
                }
            }


        }

        return true;

    }

    public boolean topologicalSort(){
        return topologicalSort(this);
    }
    // 返回拓扑排序后的名字
    public List<String> topologicalSortNames(SelfGraph selfGraph){
        // 返回null的条件
        if(selfGraph == null){
            return null;
        }
        if(selfGraph.graphNodesMap == null || selfGraph.graphNodesMap.isEmpty()){
            return null;
        }

        selfGraph.topologicalSort();
        List<Node> topologicalSortList = selfGraph.topologicalSortList;
        if(topologicalSortList.size() == 0){
            return  null;
        }
        List<String> list = new ArrayList<>();
        for (Node n :
                topologicalSortList) {
            list.add(selfGraph.nodeHashName.get(n));
        }
        return list;
    }

    public List<String> topologicalSortNames(){
        return topologicalSortNames(this);
    }





    public Double min(List<Double> a) {
        if (a == null || a.size() == 0) {
            return null;
        } else {
            double min = a.get(0);
            for (Double d :
                    a) {
                if (d < min) {
                    min = d;
                }
            }
            return min;
        }
    }


    class Node {

        String name = null;
        boolean isVisited = false;

        // 增加这一字段，纯粹是为了拓扑排序所使用
        int preNodeNums = 0;

        // 存储后继和前驱的所有节点
        // 和二叉树很像，这里nextNodelist是该节点指向的下一个节点集合
        // preNodeList 是所有指向它的节点
        List<Node> preNodeList = null;
        List<Node> nextNodeList = null;

        // 存储边，以该点为出发节点
        // 末节点为键，值为边
        HashMap<Node, Edge> edgesMap = null;

        // 内部类不同的构造器
        public Node(String name, List<Node> preNodeList, List<Node> nextNodeList) {
            this.name = name;
            this.preNodeList = preNodeList;
            this.nextNodeList = nextNodeList;
            edgesMap = new HashMap<>();
        }

        public Node(String name) {
            this.name = name;
            preNodeList = new ArrayList<>();
            nextNodeList = new ArrayList<>();
            edgesMap = new HashMap<>();
        }


        public Node(String name, List<Node> preNodeList) {

            this.name = name;
            this.preNodeList = preNodeList;
            nextNodeList = new ArrayList<>();
            edgesMap = new HashMap<>();
        }
    }

    class Edge {

        // 初末节点
        Node firstNode;
        Node endNode;
        double length;

        public Edge(Node firstNode, Node endNode, double length) {
            this.firstNode = firstNode;
            this.endNode = endNode;
            this.length = length;
        }

        public Edge(Node firstNode, Node endNode) {
            this.firstNode = firstNode;
            this.endNode = endNode;
            // 默认为1
            length = 1;
        }
    }


}
