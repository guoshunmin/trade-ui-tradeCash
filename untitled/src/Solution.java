import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.StringTokenizer;

public class Solution {
    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;

    static final class Point {
        final BigInteger xNum, xDen;
        final BigInteger yNum, yDen;

        Point(BigInteger xNum, BigInteger xDen, BigInteger yNum, BigInteger yDen) {
            BigInteger[] xn = normalizeFraction(xNum, xDen);
            BigInteger[] yn = normalizeFraction(yNum, yDen);
            this.xNum = xn[0]; this.xDen = xn[1];
            this.yNum = yn[0]; this.yDen = yn[1];
        }

        private static BigInteger[] normalizeFraction(BigInteger num, BigInteger den) {
            if (den.signum() < 0) {
                num = num.negate();
                den = den.negate();
            }
            BigInteger g = num.gcd(den);
            if (!g.equals(ONE)) {
                num = num.divide(g);
                den = den.divide(g);
            }
            return new BigInteger[]{num, den};
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point p)) return false;
            return xNum.equals(p.xNum) && xDen.equals(p.xDen)
                    && yNum.equals(p.yNum) && yDen.equals(p.yDen);
        }

        @Override
        public int hashCode() {
            return Objects.hash(xNum, xDen, yNum, yDen);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        long[][] seg = new long[n][4];
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            seg[i][0] = Long.parseLong(st.nextToken());
            seg[i][1] = Long.parseLong(st.nextToken());
            seg[i][2] = Long.parseLong(st.nextToken());
            seg[i][3] = Long.parseLong(st.nextToken());
        }

        HashSet<Point> pts = new HashSet<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Point p = getIntersection(
                        seg[i][0], seg[i][1], seg[i][2], seg[i][3],
                        seg[j][0], seg[j][1], seg[j][2], seg[j][3]
                );
                if (p != null) pts.add(p);
            }
        }
        System.out.println(pts.size());
    }

    private static Point getIntersection(long x1, long y1, long x2, long y2,
                                         long x3, long y3, long x4, long y4) {
        BigInteger X1 = BigInteger.valueOf(x1), Y1 = BigInteger.valueOf(y1);
        BigInteger X2 = BigInteger.valueOf(x2), Y2 = BigInteger.valueOf(y2);
        BigInteger X3 = BigInteger.valueOf(x3), Y3 = BigInteger.valueOf(y3);
        BigInteger X4 = BigInteger.valueOf(x4), Y4 = BigInteger.valueOf(y4);

        BigInteger dx1 = X2.subtract(X1);
        BigInteger dy1 = Y2.subtract(Y1);
        BigInteger dx2 = X4.subtract(X3);
        BigInteger dy2 = Y4.subtract(Y3);

        BigInteger denom = dx1.multiply(dy2).subtract(dy1.multiply(dx2));

        // Parallel or collinear
        if (denom.equals(ZERO)) {
            // If collinear, original behavior: ignore (return null)
            return null;
        }

        BigInteger dx31 = X3.subtract(X1);
        BigInteger dy31 = Y3.subtract(Y1);

        BigInteger numT = dx31.multiply(dy2).subtract(dy31.multiply(dx2));
        BigInteger numU = dx31.multiply(dy1).subtract(dy31.multiply(dx1));

        boolean tInRange, uInRange;
        if (denom.signum() > 0) {
            tInRange = numT.signum() >= 0 && numT.compareTo(denom) <= 0;
            uInRange = numU.signum() >= 0 && numU.compareTo(denom) <= 0;
        } else {
            tInRange = numT.signum() <= 0 && numT.compareTo(denom) >= 0;
            uInRange = numU.signum() <= 0 && numU.compareTo(denom) >= 0;
        }

        if (tInRange && uInRange) {
            // intersection point = (X1 + t * dx1, Y1 + t * dy1)
            // where t = numT / denom
            return new Point(X1.multiply(denom).add(numT.multiply(dx1)), denom,
                             Y1.multiply(denom).add(numT.multiply(dy1)), denom);
        }
        return null;
    }
}