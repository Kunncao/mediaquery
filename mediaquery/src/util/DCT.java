package util;


public class DCT {
	// C(u)
	private double[] C;
	// cached cos
	private double[][] cos;
	private int blockSize;

	public DCT () {
		this(8);
	}
	
	public DCT (int blockSize) {
		this.blockSize = blockSize;
		C = getC(blockSize);
		cos = cacheCos(blockSize);
	}
	
	/**
	 * compute C(u) u=0 or u!=0 
	 * @param blockSize N
	 * @return C(u) array
	 */
	public double[] getC(int blockSize) {
		double[] C = new double[blockSize];
		C[0] = Math.sqrt(1.0 / blockSize);
		for (int i = 1; i < blockSize; i++)
			C[i] = Math.sqrt(2.0 / blockSize);
		
		return C;
	}
	
	/**
	 * cache cosine values 
	 */
	public double[][] cacheCos(int blockSize) {
		int N = blockSize;
		double[][] cos = new double[N][N];
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				cos[i][j] = Math.cos((i + 0.5) * Math.PI * j / N);
			}
		}
		
		return cos;
	}
	
	/**
	 * compute DCT block
	 * @param f input block
	 * @return block after DCT
	 */
	public double[][] doDCT(int[][] f) {
		// block size
		int N = f.length;
		// F(u, v)
		double[][] F = new double[N][N];
		double[][] tmp = new double[N][N];
		
		// simplified DCT operation
		// F = AfA^T
		// A(i, j) = c(i)cos((j + 0.5)pi*i/N)
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					tmp[i][j] += C[i] * cos[k][i] * f[k][j];
				}
			}
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					F[i][j] += tmp[i][k] * C[j] * cos[k][j];
				}
			}
		}
		
		return F;
	}
	
	/**
	 * do inverse DCT
	 * @param F DCT block after dequantization
	 * @return reconstruct block f
	 */
	public int[][] doIDCT(double[][] F) {
		// block size
		int N = F.length;
		
		// f(x, y)
		int[][] f = new int[N][N];
		double[][] tmp = new double[N][N];
		
		// f = A^-1F(A^-1)^T
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				for (int k = 0; k < N; k++) {
					tmp[i][j] += C[k] * F[i][k] * cos[j][k];
				}
			}
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				double result = 0;
				for (int k = 0; k < N; k++) {
					result += C[k] * tmp[k][j] * cos[i][k];
				}
				f[i][j] = (int) Math.round(result);
				// prevent overflow
				f[i][j] = f[i][j] > 255 ? 255 : f[i][j];
				f[i][j] = f[i][j] < 0 ? 0 : f[i][j];
			}
		}
		
		return f;
	}
	
	
	public int getBlockSize() {
		return blockSize;
	}
}
