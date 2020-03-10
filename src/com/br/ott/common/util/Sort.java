package com.br.ott.common.util;



public class Sort {
	 public static void main(String args[]) {

		  Integer  a[] = {7,3, 6, 4, 12, 8, 5, 14, 1};
		  aprint(0,a);
//		  mergeSort(a);
		  bubbleSort(a);
		  aprint(99,a);

		 }

		 public static void mergeSort(Integer a[]) {

		  Integer b[] = new Integer[a.length];
		  int s = 1;
		  while (s < a.length) {
		   mergePass(a, b, s);// 合并到数组b
		   s += s;
		   mergePass(b, a, s);// 合并到数组a
		   s += s;
		  }
		 }
		 
		 /*
		  * 合并排好序的相邻数组段
		 */
		 public static void mergePass(Integer a[], Integer b[], Integer s) {

		  // 合并大小为s的相邻数组

		  int i = 0;
		  while (i <= (a.length - 2 * s)) {
		   // 合并大小为s的相邻二段子数组
		   merge(a, b, i, i + s - 1, i + 2 * s - 1);
		   i = i + 2 * s;
		  }
		  // 剩下的元素个数小于2s
		  if ((i + s) < a.length)
		   merge(a, b, i, i + s - 1, a.length - 1);
		  else
		   // 复制到y
		   for (int j = i; j < a.length; j++)
		    b[j] = a[j];
		 }

		 public static void merge(Integer a[], Integer b[], Integer l, int m, int r) {

		  // 合并c[l:m]和c[m+1:r]到d[l:r]
		  int i = l, j = m + 1, k = l;
		  while ((i <= m) && (j <= r))
		   if (a[i].compareTo(a[j])<=0)
		    b[k++] = a[i++];
		   else
		    b[k++] = a[j++];
		  if (i > m)
		   for (int q = j; q <= r; q++)
		    b[k++] = a[q];

		  else
		   for (int q = i; q <= m; q++)
		    b[k++] = a[q];
		  
		  aprint(l,a);

		 }

		 public static void aprint(int s, Integer a[]) {

//		  System.out.println("数组长度:" + a.length);
			 System.out.println("");
		  System.out.print(s+"合并排序后输出如下:");
		  for (int i = 0; i < a.length; i++)
		   System.out.print(a[i]+" ");
		 }
	   
		 public static void bubbleSort(Integer[] args){
			 int length = args.length;
			 for(int i=0;i<length;i++){
				 for(int j=i+1;j<length;j++){
					 if(args[i]>args[j]){
						 int temp = args[i];
						 args[i] = args[j];
						 args[j] = temp;
					 }else{
					 }
				 }
			 }
		 }
		 
}
