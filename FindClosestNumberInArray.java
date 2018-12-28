function closest (num, arr) {
                var mid;
                var lo = 0;
                var hi = arr.length - 1;
                while (hi - lo > 1) {
                    mid = Math.floor ((lo + hi) / 2);
                    if (arr[mid] < num) {
                        lo = mid;
                    } else {
                        hi = mid;
                    }
                }
                if (num - arr[lo] <= arr[hi] - num) {
                    return arr[lo];
                }
                return arr[hi];
            }
            array = [2, 42, 82, 122, 162, 202, 242, 282, 322, 362];
            number = 112;
            alert (closest (number, array));
