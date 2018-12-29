  public static void main(String ...s){
        List<Integer> d  = new ArrayList();
        d.add(-1000);
        d.add(-10);
        d.add(10);
        d.add(11);
        d.add(12);
        d.add(13);
        d.add(14);
        d.add(15);
        d.add(19);

        int k = -900;
        System.out.println(find(k,d));

    }


    //Using Binary search - logN
    static int find(int k,List<Integer> source){

        int low =0;
        int high = source.size()-1;

        while (high - low > 1){
            int mid = (low +high)/2;

            if(k <source.get( mid)){
                high = mid;
            } else {
                low = mid;
            }
        }
        if(Math.abs(k-source.get(low)) > Math.abs(k-source.get(high))){
            return source.get(high);
        } else {
            return source.get(low);
        }
    }

//dung vet can, O(n)
static int bruteForce(int k,List<Integer> d){
        int minAbd = Math.abs(k-d.get(0));
        int indexFound = 0;
        for (int i =1 ; i < d.size();i++
        ) {
            int temp = Math.abs(k-d.get(i));
            if(temp < minAbd){
                minAbd = temp;
                indexFound = i;
            }
        }
        
        return d.get(indexFound);
    }
