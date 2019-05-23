public  static Map<Integer, SortedSet<String>> mostUsedWords(String[] nums, int nWord) {
        //count the frequency for each element
        HashMap<String, Integer> map = new HashMap<>();
        for (String num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        // create a min heap
        PriorityQueue<Map.Entry<String, Integer>> queue
                = new PriorityQueue<>(Comparator.comparing(e -> e.getValue()));

        //maintain a heap of size k.
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            queue.offer(entry);
            if (queue.size() > nWord) {
                queue.poll();
            }
        }

        //get all elements from the heap
        Map<Integer,SortedSet<String>> result = new TreeMap<>();
        while (queue.size() > 0) {
            Map.Entry<String,Integer> entry =  queue.poll();

            final Integer key = entry.getValue();
            if(result.containsKey(key)){
                SortedSet<String> s = result.get(key);
                s.add(entry.getKey());
            } else{
                SortedSet sortedSet = new TreeSet();
                sortedSet.add(entry.getKey());
                result.put(key,sortedSet);
            }

        }

        return result;
    }
