import java.io.*;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        InputStream inputStream = System.in;

        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream("C:\\Users\\t-idkess\\Desktop\\123\\C 15813989 Best - Copy.txt");
        } catch (Exception e) {
            System.out.print("Failed");
            return;
        }
        TaskHashCode.InputReader in = new TaskHashCode.InputReader(inputStream);
        TaskHashCode solver = new TaskHashCode();
        solver.Solve(in);
    }


    static class TaskHashCode {
        int no_rows, no_cols, no_vehicles, no_rides, bonus, no_steps;

        class Intersection {
            int row, col;

            int distance(Intersection other) {
                return Math.abs(other.col - col) + Math.abs(other.row - row);
            }
        }

        class Ride {
            Intersection startPoint, endPoint;
            int startStep, endStep;
            int index;

            public int distance() {
                return startPoint.distance(endPoint);
            }
        }

        class Vehicle implements Comparable<Vehicle> {
            int endTime;
            Intersection endPoint;
            int index;

            @Override
            public int compareTo(Vehicle o) {
                return Integer.compare(endTime, o.endTime);
            }
        }

        void Solve(InputReader in) {
            String problemString = in.Next();
            no_rows = in.NextInt();
            no_cols = in.NextInt();
            no_vehicles = in.NextInt();
            no_rides = in.NextInt();
            bonus = in.NextInt();
            no_steps = in.NextInt();


            LinkedList<Ride> ridesB = new LinkedList<>();

            for (int i = 0; i < no_rides; i++) {
                Ride r = new Ride();

                r.startPoint = new Intersection();
                r.startPoint.row = in.NextInt();
                r.startPoint.col = in.NextInt();

                r.endPoint = new Intersection();
                r.endPoint.row = in.NextInt();
                r.endPoint.col = in.NextInt();

                r.startStep = in.NextInt();
                r.endStep = in.NextInt();

                r.index = i;
                ridesB.add(r);
            }
            double factor = 1;
            int bestScore = 0;

            ArrayList<ArrayList<Integer>> bestPlan = new ArrayList<>();
            Random r = new Random();

            while (true) {
                factor = r.nextDouble() * 0.00001;
                if (r.nextBoolean()) factor *= -1;
                LinkedList<Ride> rides = (LinkedList<Ride>) ridesB.clone();
                Collections.shuffle(rides);

                int score = 0;

                ArrayList<ArrayList<Integer>> plan = new ArrayList<>();
                for (int i = 0; i < no_vehicles; i++) plan.add(new ArrayList<>());

                rides.sort(Comparator.comparingInt(o -> o.startStep));

                PriorityQueue<Vehicle> q = new PriorityQueue<>();
                for (int i = 0; i < no_vehicles; i++) {
                    Vehicle v = new Vehicle();
                    v.endTime = 0;
                    v.endPoint = new Intersection();
                    v.endPoint.row = 0;
                    v.endPoint.col = 0;
                    v.index = i;
                    q.add(v);
                }
                boolean isBonus = false;
                while (!q.isEmpty() && !rides.isEmpty()) {
                    Vehicle v = q.poll();

                    int endTime = 0;
                    int bestStartTime = Integer.MAX_VALUE;
                    Ride bestRide = null;
                    for (Ride ride : rides) {
                        if (r.nextDouble() < 0.0001) continue;

                        if (ride.startStep > bestStartTime) break;
                        int st = Math.max(v.endTime + v.endPoint.distance(ride.startPoint), ride.startStep);
                        if (st + ride.distance() > ride.endStep) continue;

                        boolean isBonus1 = st == ride.startStep;
                        int originst = st;
                        st -= isBonus1 ? bonus : 0;
                        st += ride.distance() * factor;
                        if (st > bestStartTime) continue;
                        isBonus = isBonus1;
                        bestStartTime = st;
                        bestRide = ride;
                        endTime = originst + ride.distance();
                    }
                    if (bestRide != null) {
                        plan.get(v.index).add(bestRide.index);
                        rides.remove(bestRide);
                        v.endPoint = bestRide.endPoint;
                        v.endTime = endTime;
                        score += (isBonus ? bonus : 0) + bestRide.distance();
                        q.add(v);
                    }
                }
                if (score > bestScore) {
                    bestPlan = plan;
                    bestScore = score;

                    try {
                        PrintWriter out = new PrintWriter(new FileOutputStream("C:\\Users\\idkess\\Desktop\\hashCode\\" + problemString + " " + bestScore + ".txt"));
                        for (int i = 0; i < no_vehicles; i++) {
                            out.print(bestPlan.get(i).size());
                            for (int j = 0; j < bestPlan.get(i).size(); j++) {
                                out.print(" ");
                                out.print(bestPlan.get(i).get(j));
                            }
                            out.println();
                        }
                        out.close();
                    } catch (Exception e) {
                        System.out.print("Failed");
                        for (int i = 0; i < no_vehicles; i++) {
                            System.out.print(bestPlan.get(i).size());
                            for (int j = 0; j < bestPlan.get(i).size(); j++) {
                                System.out.print(" ");
                                System.out.print(bestPlan.get(i).get(j));
                            }
                            System.out.println();
                        }
                        return;
                    }
                    System.out.println(score);
                }
            }
        }

        static class InputReader {
            BufferedReader reader;
            StringTokenizer tokenizer;

            InputReader(InputStream stream) {
                reader = new BufferedReader(new InputStreamReader(stream), 32768);
                tokenizer = null;
            }

            String Next() {
                while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                    try {
                        tokenizer = new StringTokenizer(reader.readLine(), " \t\n\r\f,");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return tokenizer.nextToken();
            }

            int NextInt() {
                return Integer.parseInt(Next());
            }
        }
    }
}