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

        System.out.println(find(k,d));

    }


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
