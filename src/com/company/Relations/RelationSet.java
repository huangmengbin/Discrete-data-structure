package com.company.Relations;
import java.util.*;



class RelationSet<T> {
    private int size=0;
    int getSize() {
        return size;
    }
    private ArrayList<ArrayList<RelationGraphNode<T>>> graphNodes;
    ArrayList<RelationGraphNode<T>> getGraphNodes(int i) {
        return graphNodes.get(i);
    }
    private ArrayList<RelationProperty> properties = new ArrayList<>();
    RelationProperty getPropertyOfGraph(int i){return properties.get(i);}
    private RelationProperty property = RelationProperty.common;

    private Relation<T> relation;
    private MyMap<T> myMap ;
    Relation<T>getRelation(){return relation;}

    RelationSet(Collection<T> collection , Relation<T> relation ,RelationProperty property){
        this.property = property;
        create(new HashSet<>(collection) , relation) ;
    }
    RelationSet(Collection<T> collection , Relation<T> relation ) {
        this.create(new HashSet<>(collection) , relation) ;
    }
    RelationSet(MyMap<T> myMap, RelationProperty property){
        this.property = property;
        this.create(myMap);
    }
    RelationSet(MyMap<T> myMap){
        this.create(myMap);
    }

    private void create(Set<T> set, Relation<T>relation){
        MyMap<T> myMap = new MyMap<>();
        for(T current:set){
            for(T next:set){
                if(relation.relative(current,next)){
                    myMap.add(current,next);
                }
            }
        }
        this.create(myMap);
    }
    private void create(MyMap<T> _myMap){
        this.myMap    = new MyMap<>(_myMap);//防止外部 myMap 改变？其实还是有风险的...
        this.relation = (a,b) -> this.myMap.get(a)!=null&& this.myMap.get(a).contains(b) ;
    }

    void initial(RelationProperty property){
        this.property = property;
        initial1();
    }
    private void initial1(){
        initial2( this.myMap.ketAndValueSet() );
    }
    private void initial2(Set<T> set){ //
        LinkedList<ArrayList<T>> lists = toWeakConnect (set);//1.变成多个弱连通图
        this.size = lists.size();
        ArrayList<ArrayList<RelationGraphNode<T>>> result = new ArrayList<>();//??? 返回性质？？

        for(ArrayList<T>list:lists){
            initial3(list,result);
        }
        this.graphNodes = result;
    }
    private void initial3(ArrayList<T> list, List<ArrayList<RelationGraphNode<T>>> result){
        RelationSquareMatrix matrix = new RelationSquareMatrix( toBooleans(list) );///<--------------- 以后想办法避免矩阵的使用
        if( this.property==RelationProperty.order ||  matrix.isIrSymmetric() & matrix.isTransitive() ) {//O(n^3)，判断传递；如果是传递的让其非传递
            initialOrder(list,matrix,result);
        }
        else {
            ArrayList<RelationGraphNode<T>> nodeList=new ArrayList<>();
            for (T t : list) {
                nodeList.add(new RelationGraphNode<>(t));
            }
            for(int i=0;i<list.size();i++) {
                for(int j=0;j<list.size();j++){
                    if(matrix.get(i,j) &&  i!=j ) { // i R j ,不希望在这里拥有自反
                        nodeList.get(i).next.add(nodeList.get(j));
                        nodeList.get(j).before.add(nodeList.get(i));
                    }
                }
            }
            result.add(nodeList);
            if(matrix.isEquivalence()){
                properties.add(RelationProperty.equivalence);
            }
            else {
                properties.add(RelationProperty.common);
            }
        }
    }
    private void initialOrder(ArrayList<T>list,RelationSquareMatrix matrix,List<ArrayList<RelationGraphNode<T>>>result){
        ArrayList<RelationGraphNode<T>> nodes = toNodeList(list,matrix);
        ArrayList<RelationGraphNode<T>> split = split((nodes));
        result.add(split);
        properties.add(RelationProperty.order);
    }

    private boolean[][] toBooleans (List<T> list){
        boolean[][] booleans = new boolean[list.size()][list.size()];
        int i=0;
        for(T first:list){
            int j=0;
            for(T second:list){
                if(relation.relative(first,second)){//get(i),get(j)有关系,//这里不一定用顺序表
                    booleans[i][j] = true ;
                }
                j++;
            }
            i++;
        }
        return booleans;
    }

    private ArrayList<RelationGraphNode<T>> toNodeList(ArrayList<T>list, RelationSquareMatrix matrix){//已知它是偏序或拟序，并且我们已经消去它不必要的传递性
        ArrayList<RelationGraphNode<T>> nodes = new ArrayList<>();
        for(T i: list){
            nodes.add(new RelationGraphNode<>( i ));
        }
        for(int i=0;i<list.size();++i){
            for(int j=0;j<list.size();++j){
                if(i!=j){//如果自反的话，消除之
                    if(matrix.get(i,j)){    // i R j
                        nodes.get(i).next.add(nodes.get(j));
                        nodes.get(j).before.add(nodes.get(i));
                    }
                }
            }
        }
        return nodes;
    }

    private ArrayList<RelationGraphNode<T>> toReversedTopologicalChains(List<RelationGraphNode<T>> nodes){
        //求一个拓扑排序的翻转，传入的必须是拟序or偏序；否则可能得不到想要的结果。时间应该是O(V+E)


        HashMap<RelationGraphNode<T>,Integer> hashMap = new HashMap<>();
        ArrayList<RelationGraphNode<T>> result = new ArrayList<>();
        for(RelationGraphNode<T> node:nodes){
            hashMap.put(node , node.next.size());
            if(node.next.size()==0){
                result.add(node);//极大元都入列
            }
        }
        if(result.isEmpty()){throw new ArithmeticException("找不到极大元，该路径可能存在环路");}

        int ptr=0;
        while(ptr<result.size()){
            final RelationGraphNode<T> node = result.get(ptr);
            for(RelationGraphNode<T>beforeNode:node.before){
                final int number = hashMap.get(beforeNode)-1;
                hashMap.put(beforeNode,number);
                if(number==0){
                    result.add(beforeNode);
                }
            }
            ptr++;
        }

        return result;
    }

    private ArrayList<RelationGraphNode<T>> split (ArrayList<RelationGraphNode<T>> nodes){//nodes 用后作废; 依赖于非传递,否则复杂度会爆炸
        ArrayList<RelationGraphNode<T>> tempList = this.toReversedTopologicalChains (nodes);// 拓扑排序，注意是 从大到小

        this.calculateDepth   (tempList);
        this.removeTransitive (tempList);

        /*
        while (!queue.isEmpty()){
            RelationGraphNode<T> first = queue.removeFirst();
            for(RelationGraphNode<T> node:first.before) {       //O(V^2)
                if(first.depth +1>node.depth){
                    node.depth =first.depth +1;
                    queue.add(node);
                }
            }
        }*/

        tempList.clear();
        nodes.sort( (a,b) -> {
            if(a.depth==b.depth){
                return a.hashCode() - b.hashCode();
            }
            else {
                return a.depth - b.depth;
            }
        } ); //相信它是n*logn的排序


        ArrayList<ArrayList<RelationGraphNode<T>>> tmpResults=new ArrayList<>();

        int depthNumber = 0;

        ListIterator<RelationGraphNode<T>> listIterator = nodes.listIterator();
        while (listIterator.hasNext()) {
            RelationGraphNode<T> node = listIterator.next();
            if (node.depth == depthNumber) {
                ArrayList<RelationGraphNode<T>> arrayList = new ArrayList<>();
                arrayList.add(node);
                tmpResults.add(arrayList);
            } else {
                ++depthNumber;
                listIterator.previous();
                break;
            }
        }

        ArrayList<RelationGraphNode<T>> currentList = new ArrayList<>();
        HashMap<RelationGraphNode<T>,RelationGraphNode<T>> match     = new HashMap<>();
        HashMap<RelationGraphNode<T>,RelationGraphNode<T>> matchedBy = new HashMap<>();

        //int hhhhh=0;
        while (true) {

            while (listIterator.hasNext()) {
                RelationGraphNode<T> tmpNode = listIterator.next();
                tmpNode.valid=true;         //initial
                if(tmpNode.depth == depthNumber){
                    currentList.add(tmpNode);
                }
                else {                      //仍有next，只是深度更进了一层
                    ++depthNumber;
                    listIterator.previous();//恢复
                    break;//jump out
                }
            }

            {
                for(ArrayList<RelationGraphNode<T>>arrayList:tmpResults){
                    tempList.add(arrayList.get(arrayList.size()-1));
                }
            }

            {
                for(RelationGraphNode<T> node:currentList){
                    dfs(node,tempList,match,matchedBy);
                }
            }



            {
                for(ArrayList<RelationGraphNode<T>>arrayList:tmpResults){
                    RelationGraphNode<T> node = matchedBy.get(arrayList.get(arrayList.size()-1));
                    if(node!=null){
                        arrayList.add(node);
                    }
                }

                for(RelationGraphNode<T> hmb : currentList){
                    if(match.get(hmb)==null){       //<--- hmb 找不到心仪的对象
                        ArrayList<RelationGraphNode<T>>arrayList=new ArrayList<>();
                        arrayList.add(hmb);         //<--- 所以就只能孤身了
                        tmpResults.add(arrayList);
                    }
                }
            }


            if( ! listIterator.hasNext()) {
                break;
             }
            else {

                for(RelationGraphNode<T>node:currentList) node.valid=true; //<--- 恢复 valid
                match.clear();
                matchedBy.clear();
                tempList.clear();
                currentList.clear();
            }


        } // end while


        for(RelationGraphNode n:nodes) n.valid=true;
        tmpResults.sort(Comparator.comparingInt(a ->  - a.size() ));

        ArrayList<RelationGraphNode<T>> result = new ArrayList<>();//微调以下顺序而已
        for (ArrayList<RelationGraphNode<T>> tmpResult : tmpResults) {
            result.addAll(tmpResult);
            result.add(null);
        }
        return result;

    }

    private void calculateDepth(List<RelationGraphNode<T>> tempList){
        for(RelationGraphNode<T>node:tempList){ //小心是反序 ;  O(V+E) 还是 O(V^2） ？; 用来计算深度
            if(node.next.isEmpty()){
                node.depth=0;
            }
            else {
                int maxNum=-1;
                for(RelationGraphNode<T>beforeNode:node.next){
                    if(beforeNode.depth>maxNum){
                        maxNum=beforeNode.depth;
                    }
                }
                node.depth=maxNum + 1 ;
            }
        }//end for,此时深度计算完成
    }

    private void removeTransitive(List<RelationGraphNode<T>> nodes){
        for(RelationGraphNode<T>node:nodes){
            LinkedList<RelationGraphNode<T>> before = new LinkedList<>();
            for(RelationGraphNode<T>node1:node.before){
                if(node1.depth==node.depth+1){
                    before.add(node1);
                }
            }
            node.before=before;

            LinkedList<RelationGraphNode<T>> next   = new LinkedList<>();
            for(RelationGraphNode<T>node1:node.next){
                if(node1.depth+1==node.depth){
                    next.add(node1);
                }
            }
            node.next=next;
        }
    }

    private boolean symmetricOfRelation (T first, T second){
        return relation.relative(first,second) || relation.relative(second,first);
    }

    private LinkedList<ArrayList<T>> toWeakConnect(Set<T> set){/*等价类的划分：如原来不是等价关系，则转成等价闭包。
        用来划分弱连通分支
        用并查集会也行？但好像没必要？？*/

        LinkedList<ArrayList<T>> result = new LinkedList<>();


        for(T element : set) { /*原来的 elementList
             3重循环，时间为 O (n^2)；应该是最低的限度了？*/

            ArrayList<T> tmp = new ArrayList<>();
            tmp.add(element);

            ListIterator<ArrayList<T>> resultIterator = result.listIterator(0);

            ArrayList<T> firstHit = null ;//<-----------------可以尽可能的维护原来的升序

            while(resultIterator.hasNext()) {//
                ArrayList<T> arrayList = resultIterator.next();

                for(T oldElement:arrayList) {//

                    if(symmetricOfRelation(element , oldElement)){//hit

                        if(firstHit==null){//第一次命中
                            firstHit=arrayList;
                        }
                        else {//再次命中，归类
                            firstHit.addAll(arrayList);
                            resultIterator.remove();//必须移除linkedList，不然重复
                        }
                        break;      //不需要移除element , 直接退出最内层for循环即可
                    }

                }//end for
            }//end while
            if(firstHit==null){//未命中
                result.add(tmp);//暂时孤立
            }
            else {//至少命中一次
                firstHit.add(element);//相当于 firstHit.addAll(tmp);
            }

        }//end for

        return result;////<<<<<<-------------------------------------------------------
    }

    LinkedList<ArrayList<T>> tarjan(){
        return null;
    }

    private boolean dfs (RelationGraphNode<T> node, ArrayList<RelationGraphNode<T>>tmpList, HashMap<RelationGraphNode<T>,RelationGraphNode<T>>match,HashMap<RelationGraphNode<T>,RelationGraphNode<T>>matchBy){
        if(!node.valid)throw new RuntimeException();
        node.valid = false;
        boolean find = false;
        for(RelationGraphNode<T>nextNode : tmpList ){ ///【迫不得已，理论上应该用 node.next 每单次可达到O(V+E)而不是O(V^2)//???
            if(nextNode.valid && relation.relative( node.data , nextNode.data)) {
                find=dfs2(nextNode,tmpList, match, matchBy);
            }
            if(find){
                match.put(node,nextNode);
                matchBy.put(nextNode,node);
                break;
            }
        }
        node.valid=find;// 如果找不到，则说明这条路行不通，以后都不搜这里了。这是提高效率的关键
        return find;
    }
    private boolean dfs2(RelationGraphNode<T> node, ArrayList<RelationGraphNode<T>>tmpList,HashMap<RelationGraphNode<T>,RelationGraphNode<T>>match,HashMap<RelationGraphNode<T>,RelationGraphNode<T>>matchBy){
        if(!node.valid)throw new RuntimeException();
        node.valid = false;
        boolean find ;
        if(matchBy.get(node)==null){
            find = true;
        }
        else {
            find = dfs(matchBy.get(node),tmpList,match,matchBy);
        }
        node.valid=find;// 如果找不到，则说明这条路行不通，以后都不搜这里了
        return find;
    }
    /**
     ArrayList<ArrayList<RelationGraphNode<T>>> tmpResults=new ArrayList<>();
     ListIterator<RelationGraphNode<T>> listIterator ;
     RelationGraphNode<T> tmpBefore ;
     RelationGraphNode<T> current   ;
     while (!nodes.isEmpty()) {
     tmpBefore=null;
     listIterator=nodes.listIterator(0);
     ArrayList<RelationGraphNode<T>> tmpResult = new ArrayList<>();
     while (listIterator.hasNext()){
     current=listIterator.next();
     //最好要尽可能保证 tmpBefore R current ，也就是二分图的最大匹配;算了，有空再搞吧。
     if(tmpBefore==null  ||  (tmpBefore.depth+1==current.depth)&(relation.relative(current.data,tmpBefore.data))){ ////对于后者，  ( tmpBefore 已经 != null )
     tmpResult.add(current);
     tmpBefore=current;
     listIterator.remove();
     }
     else if(tmpBefore.depth+2==current.depth){
     //System.out.print(current+" ");
     current=listIterator.previous();
     if(listIterator.hasPrevious()){
     listIterator.previous();
     current=listIterator.next();
     }
     //System.out.println(current);
     tmpResult.add(current);
     tmpBefore=current;
     listIterator.remove();
     }
     else if(tmpBefore.depth+2<current.depth){
     System.out.println(current);
     }
     }
     tmpResult .add(null); //这算是一个 分隔符 ，和 标记符
     tmpResults.add(tmpResult);
     }



     ArrayList<RelationGraphNode<T>> result = new ArrayList<>();//微调以下顺序而已
     for(int i=(tmpResults.size()-1)/2*2;i>=0;i-=2){
     result.addAll(tmpResults.get(i));
     }
     for(int i=1;i<tmpResults.size();i+=2){
     result.addAll(tmpResults.get(i));
     }
     return result;
     */





}
