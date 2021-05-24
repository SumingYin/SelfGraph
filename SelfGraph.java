package com.ysm.offer;

import java.util.*;

/**
 * 实现图的数据结构
 * 可赋予每个节点String名字
 */
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


    // 初始化工作
    public SelfGraph() {
        graphNodesMap = new HashMap<>();
        nodeHashName = new HashMap<>();
    }

    public static void main(String[] args) {
        SelfGraph selfGraph = new SelfGraph();
        int nodeNum = 5;
        double[][] nodes = new double[nodeNum][nodeNum];

        for (int i = 0; i < nodeNum; i++) {
            for (int j = 0; j < nodeNum; j++) {

                Random random = new Random();
                int num = random.nextInt(2);
                if (num > 0) {
                    if (i == j && num > 0) {
                        continue;
                    }
                    nodes[i][j] = 1;
                }
            }
        }

        selfGraph.createDirectedGraph(nodeNum, nodes);
        System.out.println(selfGraph);

        /*
        根据名字查到两者节点的索引，这两个节点之间的长度是多少
        根据index查到两个节点引用，这两个几点之间的长度是多少
         */

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
        if (Matrix.length != n || Matrix.length == 0) {
            return null;
        } else if (Matrix.length != Matrix[0].length) {
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
        if (Matrix.length != n || Matrix.length == 0) {
            return null;
        } else if (Matrix.length != Matrix[0].length) {
            return null;
        } else if (names.size() != n) {
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
        // 如果已经存在则无法添加成功
        if (name == null || name.length() == 0 || graphNodesMap.containsKey(name)) {
            return false;
        }
        defaultName++;
        nodeNums++;
        Node node1 = new Node(name);
        graphNodesMap.put(name, node1);
        return true;
    }

    /**
     * @param startNode 初始节点
     * @param endNode   末尾节点
     * @param len       边的长度
     * @return
     */
    public boolean addNextEdge(Node startNode, Node endNode, double len) {
        if (startNode == null || endNode == null || !graphNodesMap.containsValue(startNode) || graphNodesMap.containsValue(endNode) || len <= 0) {
            return false;
        }
        List<Node> list = new ArrayList<>();
        List<Double> list1 = new ArrayList<>();
        list.add(endNode);
        list1.add(len);
        return addNextEdge(startNode, list, list1);
    }

    // 同过给定名字的方式添加节点
    public boolean addNextEdge(String startName, String endName, double len) {
        if (endName == null || startName == null || startName.length() == 0 || endName.length() == 0 || len <= 0) {
            return false;
        }
        return addNextEdge(graphNodesMap.get(startName), graphNodesMap.get(endName), len);
    }

    public boolean addNextEdge(Node startNode, List<Node> endNodes, List<Double> lengths) {

        // 如果图中不含有startNode，或传入的值为null，则返回false
        if (!graphNodesMap.containsValue(startNode) || startNode == null || endNodes == null || lengths == null) {
            return false;
        }
        int size = endNodes.size();
        // 需要先检查所有添加的边是否符合条件
        for (int i = 0; i < size; i++) {
            Node endNode = endNodes.get(i);
            Double aDouble = lengths.get(i);
            if (endNode != null && graphNodesMap.containsValue(endNode) && aDouble > 0) {
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
            if (startNode != null && graphNodesMap.containsValue(startNode) && aDouble > 0) {
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

        if (startNode == null || endNode == null || !graphNodesMap.containsValue(startNode) || graphNodesMap.containsValue(endNode) || len <= 0) {
            return false;
        }
        List<Node> list = new ArrayList<>();
        List<Double> list1 = new ArrayList<>();
        list.add(startNode);
        list1.add(len);
        return addNextEdge(endNode, list, list1);

    }

    public boolean addPreEdge(String endName, String startName, double len) {
        if (endName == null || startName == null || startName.length() == 0 || endName.length() == 0 || len <= 0) {
            return false;
        }
        return addPreEdge(graphNodesMap.get(endName), graphNodesMap.get(startName), len);
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
        if(name == null || name.length()==0 || !nodeHashName.containsValue(name)){
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
        return get2NodesLength(graphNodesMap.get(startName), graphNodesMap.get(endName));
    }

    public double get2NodesLength(Node startNode, Node endNode) {

        if (startNode == null || endNode == null || !graphNodesMap.containsValue(startNode) || !graphNodesMap.containsValue(endNode)) {
            // 返回负数，说明出错了
            return -1.0;
        }

        HashMap<Node, Edge> edgesMap = startNode.edgesMap;
        Edge edge = edgesMap.get(endNode);
        return edge.length;

    }

    public double shortestPath2Nodes(Node startNode, Node endNode) {

        // 首先判断是否可到达
        // 如果可到达就计算最短的长度
        return 1.0;
    }


    class Node {

        String name = null;

        // 存储后继和前驱的所有节点
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
