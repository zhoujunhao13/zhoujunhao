package com.zjh.test;

public class SortAlgorithm {
	
	public static void main(String[] args) {
		int numbers[] = new int[5];
		numbers[0] = 4;
		numbers[1] = 3;
		numbers[2] = 2;
		numbers[3] = 1;
		numbers[4] = 5;
		
		int nums[] = {1,2,3,4,5};
		System.out.println(erFenFind(nums, 1));
		erFenSort(numbers);
		for(int i=0;i<numbers.length;i++) {
			System.out.println(numbers[i]);
		}
	}

	/**
	 * 冒泡排序
	 * @param numbers
	 */
	public static void bubbleSort(int[] numbers) {
		int size = numbers.length;
		int temp;
		for(int i=0;i<size-1;i++) {
			for(int j=i+1;j<size;j++) {
				if(numbers[i] > numbers[j]) {
					temp = numbers[i];
					numbers[i] = numbers[j];
					numbers[j] = temp;
				}
			}
		}
	}
	
	
	public static void quickSort(int[] numbers) {
		
	}
	
	/**
	 * 二分法查找
	 */
	public static int erFenFind(int[] array, int x) {
		int min = 0;
		int max = array.length - 1;
		int mid;
		while(min <=max) {
			mid = (min + max)/2;
			if(array[mid] > x) {
				max = mid;
			}
			if(array[min] < x) {
				min = mid;
			}
			if(array[mid] == x) {
				return mid;
			}
		}
		return -1;
	}
	
	/**
	 * 二分法排序
	 */
	public static void erFenSort(int[] array) {
		for(int i=1;i<array.length;i++) {
			int temp = array[i];
			int low = 0;
			int hig = i-1;
			int mid = -1;
			while(low <= hig) {
				mid = low + (hig - low)/2;
				if(array[mid] > temp) {
					hig = mid -1;
				}else {
					low = mid + 1;
				}
			}
			
			for(int j=i-1; j >=low;j--) {
				array[j+1] = array[j];
			}
			array[low] = temp;
		}
	}
	
}
