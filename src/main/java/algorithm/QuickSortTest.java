package algorithm;

/**
 * Created by yao on 15/12/18.
 */
public class QuickSortTest {

    public static void main(String[]args){

        int []arr={2,9,3,2,4,8,7,10};
        quickSort(arr,0,7);
        print(arr);

    }
    public static void print(int[]arr){
        for (int i=0;i<arr.length;i++){
            System.out.print(arr[i] + "\t");
        }
        System.out.println();
    }

    public static void quickSort(int arr[],int low,int high) {
        int l=low;
        int h=high;
        int povit=arr[low];

        while(l<h){
            while(l<h&&arr[h]>=povit) h--;
            if(l<h){
                int temp=arr[h];
                arr[h]=arr[l];
                arr[l]=temp;
                l++;
            }

            while(l<h&&arr[l]<=povit) l++;

            if(l<h){
                int temp=arr[h];
                arr[h]=arr[l];
                arr[l]=temp;
                h--;
            }
        }
        print(arr);
        System.out.print("l="+(l+1)+"h="+(h+1)+"povit="+povit+"\n");
        if(l>low)quickSort(arr, low, l - 1);
        if(h<high)quickSort(arr,l+1,high);
    }



}
