package com.company.Relations;

class RelationSquareMatrix {

	private boolean[][] data;

	public boolean[][] getData() {
		return data;
	}
	public void setData(boolean[][] a){
		if(a.length!=a[0].length)throw new ArithmeticException("这不是方阵");
		data=new boolean[a.length][];
		for(int i=0 ; i < a.length ; i++ ) {
			data [i] = a [i]. clone();
		}
	}
	public void setData(int [][] a){
		boolean b [][] = new boolean[a.length][a[0].length];
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[0].length;j++)
				b[i][j] = (a[i][j]!=0) ;
		}
		setData(b);
	}



	public boolean get(int row, int column){
		return data[row][column];
	}
	public void set(int row,int column,boolean b){
		data[row][column] = b;
	}

	public RelationSquareMatrix(boolean[][] a){
		setData(a);
	}
	public RelationSquareMatrix(int [][] a){
		setData(a);
	}


	public void makeTransitive(){//传递
		for(int k=0;k<data.length;k++){
			for(int i=0;i<data.length;i++){
				for(int j=0;j<data.length;j++){
					data[i][j] = ( data[i][j]  ||  ( data[i][k] & data[k][j] ) );
				}
			}
		}
	}
	boolean isTransitive(){//传递
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data.length;j++){
					for(int k=0;k<data.length;k++){
					if(  !data[i][j] && data[i][k] && data[k][j] ){
						return false;
					}
				}
			}
		}
		return true;
	}
	boolean ifTransitiveThenMakeItNot(){

		boolean[][]booleans= new boolean[data.length][];
		for(int i=0;i<data.length;++i){
			booleans[i]=data[i].clone();
		}
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data.length;j++){
				for(int k=0;k<data.length;k++){
					if(data[i][k] && data[k][j] ){
						if(!data[i][j]) {
							return false;
						}
						else if(i!=k&&k!=j&&j!=i){//已经证明反对称； 防止自反
							booleans[i][j]=false;//make it not trans
						}
					}
				}
			}
		}
		this.data=booleans;//update data ; make it not trans
		return true;
	}
	public void makeReflexive(){//自反
		for(int i=0;i<data.length;++i){
			data[i][i]=true;
		}
	}
	boolean isReflexive(){//自反
		for(int i=0;i<data.length;++i){
			if(!data[i][i]){
				return false;
			}
		}
		return true;
	}
	public void makeSymmetric(){//对称
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				data[i][j]=data[j][i]=(data[i][j]|data[j][i]);
			}
		}
	}
	boolean isSymmetric(){//对称
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				if(data[i][j]!=data[j][i]){
					return false;
				}
			}
		}
		return true;
	}
	public void makeEquivalence(){//等价
		this.makeSymmetric();
		this.makeReflexive();
		this.makeTransitive();
	}
	boolean isEquivalence(){
		return isReflexive() & isSymmetric() & isTransitive();
	}

	boolean isIrSymmetric(){//反对称
		for(int i=0;i<data.length;i++){
			for(int j=0;j<data[0].length;j++){
				if(data[i][j] && data[j][i] &&  i!=j ){
					return false;
				}
			}
		}
		return true;
	}
	boolean isPartialOrder(){
		return isIrSymmetric() & isReflexive() & isTransitive();
	}
	public RelationSquareMatrix transpose(){
		try {
			boolean [][]temp=new boolean[this.data[0].length][this.data.length];
			for(int i=0;i<this.data.length;i++){
				for(int j=0;j<this.data[0].length;j++){
					temp[j][i]=this.data[i][j];
				}
			}
			return new RelationSquareMatrix(temp);
		}catch (Exception e){
			e.printStackTrace();
			return new RelationSquareMatrix(new boolean[0][0]);
		}
	}
	

	public void print(){
		try {
			System.out.print('\n');
			for (int i = 0; i < this.data.length; i++) {
				printLine(i);
			}
			System.out.print('\n');
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	void printLine(int i ){
		for (int j = 0; j < this.data[0].length; j++) {
			if (j < this.data[0].length - 1) {
				System.out.print((this.data[i][j]?1:0) +"\t");
			} else {
				System.out.print((this.data[i][j]?1:0) +"\n");
			}
		}
	}

	public static void main(String[] args){//测试
		int [][]temp1 = new int[][]{
				{0,0,0},
				{1,1,1},
				{0,0,0},};
		RelationSquareMatrix matrix = new RelationSquareMatrix(temp1);
		matrix.makeEquivalence();
		matrix.print();
	}

}