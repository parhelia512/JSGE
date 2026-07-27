/*
 * Copyright (C) 2026 Prof. Dr. David Buzatto
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package br.com.davidbuzatto.jsge.math;

/**
 * Interface with static utility methods for procedural Perlin noise generation.
 *
 * This is a Java port of stb_perlin.h (v0.5), a public domain single-file
 * C implementation of Ken Perlin's revised (2002) noise function, written
 * by Sean Barrett. The original is dual-licensed under the MIT License and
 * the Unlicense (public domain).
 * Reference: https://github.com/nothings/stb/blob/master/stb_perlin.h
 *
 * @author Prof. Dr. David Buzatto
 */
public interface PerlinNoise {

    // base permutation/gradient tables (256 entries), taken from stb_perlin.h.
    // stb doubles them to 512 entries to avoid an extra mask when indexing,
    // so the same is done below through duplicate().

    public static final int[] RAND_TAB_BASE = {
        23, 125, 161, 52, 103, 117, 70, 37, 247, 101, 203, 169, 124, 126, 44, 123,
        152, 238, 145, 45, 171, 114, 253, 10, 192, 136, 4, 157, 249, 30, 35, 72,
        175, 63, 77, 90, 181, 16, 96, 111, 133, 104, 75, 162, 93, 56, 66, 240,
        8, 50, 84, 229, 49, 210, 173, 239, 141, 1, 87, 18, 2, 198, 143, 57,
        225, 160, 58, 217, 168, 206, 245, 204, 199, 6, 73, 60, 20, 230, 211, 233,
        94, 200, 88, 9, 74, 155, 33, 15, 219, 130, 226, 202, 83, 236, 42, 172,
        165, 218, 55, 222, 46, 107, 98, 154, 109, 67, 196, 178, 127, 158, 13, 243,
        65, 79, 166, 248, 25, 224, 115, 80, 68, 51, 184, 128, 232, 208, 151, 122,
        26, 212, 105, 43, 179, 213, 235, 148, 146, 89, 14, 195, 28, 78, 112, 76,
        250, 47, 24, 251, 140, 108, 186, 190, 228, 170, 183, 139, 39, 188, 244, 246,
        132, 48, 119, 144, 180, 138, 134, 193, 82, 182, 120, 121, 86, 220, 209, 3,
        91, 241, 149, 85, 205, 150, 113, 216, 31, 100, 41, 164, 177, 214, 153, 231,
        38, 71, 185, 174, 97, 201, 29, 95, 7, 92, 54, 254, 191, 118, 34, 221,
        131, 11, 163, 99, 234, 81, 227, 147, 156, 176, 17, 142, 69, 12, 110, 62,
        27, 255, 0, 194, 59, 116, 242, 252, 19, 21, 187, 53, 207, 129, 64, 135,
        61, 40, 167, 237, 102, 223, 106, 159, 197, 189, 215, 137, 36, 32, 22, 5
    };

    public static final int[] RAND_TAB_GRAD_IDX_BASE = {
        7, 9, 5, 0, 11, 1, 6, 9, 3, 9, 11, 1, 8, 10, 4, 7,
        8, 6, 1, 5, 3, 10, 9, 10, 0, 8, 4, 1, 5, 2, 7, 8,
        7, 11, 9, 10, 1, 0, 4, 7, 5, 0, 11, 6, 1, 4, 2, 8,
        8, 10, 4, 9, 9, 2, 5, 7, 9, 1, 7, 2, 2, 6, 11, 5,
        5, 4, 6, 9, 0, 1, 1, 0, 7, 6, 9, 8, 4, 10, 3, 1,
        2, 8, 8, 9, 10, 11, 5, 11, 11, 2, 6, 10, 3, 4, 2, 4,
        9, 10, 3, 2, 6, 3, 6, 10, 5, 3, 4, 10, 11, 2, 9, 11,
        1, 11, 10, 4, 9, 4, 11, 0, 4, 11, 4, 0, 0, 0, 7, 6,
        10, 4, 1, 3, 11, 5, 3, 4, 2, 9, 1, 3, 0, 1, 8, 0,
        6, 7, 8, 7, 0, 4, 6, 10, 8, 2, 3, 11, 11, 8, 0, 2,
        4, 8, 3, 0, 0, 10, 6, 1, 2, 2, 4, 5, 6, 0, 1, 3,
        11, 9, 5, 5, 9, 6, 9, 8, 3, 8, 1, 8, 9, 6, 9, 11,
        10, 7, 5, 6, 5, 9, 1, 3, 7, 0, 2, 10, 11, 2, 6, 1,
        3, 11, 7, 7, 2, 1, 7, 3, 0, 8, 1, 1, 5, 0, 6, 10,
        11, 11, 0, 2, 7, 0, 10, 8, 3, 5, 7, 1, 11, 1, 0, 7,
        9, 0, 11, 5, 10, 3, 2, 3, 5, 9, 7, 9, 8, 4, 6, 5
    };

    public static final int[] RAND_TAB = duplicate( RAND_TAB_BASE );
    public static final int[] RAND_TAB_GRAD_IDX = duplicate( RAND_TAB_GRAD_IDX_BASE );

    // gradient directions used by grad(), reduced to 12 cases (x, y, z).
    public static final double[][] GRADIENT_BASIS = {
        {  1,  1,  0 },
        { -1,  1,  0 },
        {  1, -1,  0 },
        { -1, -1,  0 },
        {  1,  0,  1 },
        { -1,  0,  1 },
        {  1,  0, -1 },
        { -1,  0, -1 },
        {  0,  1,  1 },
        {  0, -1,  1 },
        {  0,  1, -1 },
        {  0, -1, -1 }
    };

    /**
     * Generates 3D Perlin noise at the given coordinates using a custom seed.
     * Adjacent values are continuous, but the noise takes on unrelated values
     * at integer coordinates (period 1).
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param xWrap Wrap period for the x axis (must be a power of two, or 0 for no wrap).
     * @param yWrap Wrap period for the y axis (must be a power of two, or 0 for no wrap).
     * @param zWrap Wrap period for the z axis (must be a power of two, or 0 for no wrap).
     * @param seed Seed used to select between different variations of the noise (only the lowest 8 bits are used).
     * @return The noise value at the given coordinates.
     */
    public static double noise( double x, double y, double z, int xWrap, int yWrap, int zWrap, int seed ) {
        return noise3Internal( x, y, z, xWrap, yWrap, zWrap, seed & 255 );
    }

    /**
     * Generates 3D Perlin noise at the given coordinates.
     * The noise always wraps every 256 units, even when xWrap/yWrap/zWrap are 0.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param xWrap Wrap period for the x axis (must be a power of two, or 0 for no wrap).
     * @param yWrap Wrap period for the y axis (must be a power of two, or 0 for no wrap).
     * @param zWrap Wrap period for the z axis (must be a power of two, or 0 for no wrap).
     * @return The noise value at the given coordinates.
     */
    public static double noise( double x, double y, double z, int xWrap, int yWrap, int zWrap ) {
        return noise3Internal( x, y, z, xWrap, yWrap, zWrap, 0 );
    }

    /**
     * Generates 3D Perlin noise at the given coordinates, without wrapping.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @return The noise value at the given coordinates.
     */
    public static double noise( double x, double y, double z ) {
        return noise( x, y, z, 0, 0, 0 );
    }

    /**
     * Generates 2D Perlin noise at the given coordinates (z fixed at 0), without wrapping.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return The noise value at the given coordinates.
     */
    public static double noise( double x, double y ) {
        return noise( x, y, 0, 0, 0, 0 );
    }

    /**
     * Generates 3D Perlin noise at the given coordinates, allowing wrap periods
     * that are not powers of two (uses modulo indexing instead of bitmasking).
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param xWrap Wrap period for the x axis (any positive value, or 0 for the default of 256).
     * @param yWrap Wrap period for the y axis (any positive value, or 0 for the default of 256).
     * @param zWrap Wrap period for the z axis (any positive value, or 0 for the default of 256).
     * @param seed Seed used to select between different variations of the noise (only the lowest 8 bits are used).
     * @return The noise value at the given coordinates.
     */
    public static double noiseWrapNonPow2( double x, double y, double z, int xWrap, int yWrap, int zWrap, int seed ) {
        return noiseWrapNonPow2Internal( x, y, z, xWrap, yWrap, zWrap, seed & 255 );
    }

    /**
     * Generates 3D fractional Brownian motion (fBm) noise, summing several
     * octaves of Perlin noise with decreasing amplitude and increasing frequency.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param lacunarity Frequency multiplier applied at each octave (commonly ~2.0).
     * @param gain Amplitude multiplier applied at each octave (commonly 0.5).
     * @param octaves Number of octaves to sum (commonly 6).
     * @return The fBm noise value at the given coordinates.
     */
    public static double fbm( double x, double y, double z, double lacunarity, double gain, int octaves ) {

        double frequency = 1.0;
        double amplitude = 1.0;
        double sum = 0.0;

        for ( int i = 0; i < octaves; i++ ) {
            sum += noise3Internal( x * frequency, y * frequency, z * frequency, 0, 0, 0, i & 255 ) * amplitude;
            frequency *= lacunarity;
            amplitude *= gain;
        }

        return sum;

    }

    /**
     * Generates 2D fractional Brownian motion (fBm) noise (z fixed at 0).
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param lacunarity Frequency multiplier applied at each octave (commonly ~2.0).
     * @param gain Amplitude multiplier applied at each octave (commonly 0.5).
     * @param octaves Number of octaves to sum (commonly 6).
     * @return The fBm noise value at the given coordinates.
     */
    public static double fbm( double x, double y, double lacunarity, double gain, int octaves ) {
        return fbm( x, y, 0, lacunarity, gain, octaves );
    }

    /**
     * Generates 3D ridge noise, useful for mountain ridge-like features.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param lacunarity Frequency multiplier applied at each octave (commonly ~2.0).
     * @param gain Amplitude multiplier applied at each octave (commonly 0.5).
     * @param offset Value used to invert the ridges (commonly 1.0, may need to be larger).
     * @param octaves Number of octaves to sum (commonly 6).
     * @return The ridge noise value at the given coordinates.
     */
    public static double ridge( double x, double y, double z, double lacunarity, double gain, double offset, int octaves ) {

        double frequency = 1.0;
        double amplitude = 0.5;
        double prev = 1.0;
        double sum = 0.0;

        for ( int i = 0; i < octaves; i++ ) {
            double r = noise3Internal( x * frequency, y * frequency, z * frequency, 0, 0, 0, i & 255 );
            r = offset - Math.abs( r );
            r = r * r;
            sum += r * amplitude * prev;
            prev = r;
            frequency *= lacunarity;
            amplitude *= gain;
        }

        return sum;

    }

    /**
     * Generates 2D ridge noise (z fixed at 0), useful for mountain ridge-like features.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param lacunarity Frequency multiplier applied at each octave (commonly ~2.0).
     * @param gain Amplitude multiplier applied at each octave (commonly 0.5).
     * @param offset Value used to invert the ridges (commonly 1.0, may need to be larger).
     * @param octaves Number of octaves to sum (commonly 6).
     * @return The ridge noise value at the given coordinates.
     */
    public static double ridge( double x, double y, double lacunarity, double gain, double offset, int octaves ) {
        return ridge( x, y, 0, lacunarity, gain, offset, octaves );
    }

    /**
     * Generates 3D turbulence noise, summing the absolute value of several
     * octaves of Perlin noise.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     * @param lacunarity Frequency multiplier applied at each octave (commonly ~2.0).
     * @param gain Amplitude multiplier applied at each octave (commonly 0.5).
     * @param octaves Number of octaves to sum (commonly 6).
     * @return The turbulence noise value at the given coordinates.
     */
    public static double turbulence( double x, double y, double z, double lacunarity, double gain, int octaves ) {

        double frequency = 1.0;
        double amplitude = 1.0;
        double sum = 0.0;

        for ( int i = 0; i < octaves; i++ ) {
            double r = noise3Internal( x * frequency, y * frequency, z * frequency, 0, 0, 0, i & 255 ) * amplitude;
            sum += Math.abs( r );
            frequency *= lacunarity;
            amplitude *= gain;
        }

        return sum;

    }

    /**
     * Generates 2D turbulence noise (z fixed at 0), summing the absolute value
     * of several octaves of Perlin noise.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param lacunarity Frequency multiplier applied at each octave (commonly ~2.0).
     * @param gain Amplitude multiplier applied at each octave (commonly 0.5).
     * @param octaves Number of octaves to sum (commonly 6).
     * @return The turbulence noise value at the given coordinates.
     */
    public static double turbulence( double x, double y, double lacunarity, double gain, int octaves ) {
        return turbulence( x, y, 0, lacunarity, gain, octaves );
    }

    private static double noise3Internal( double x, double y, double z, int xWrap, int yWrap, int zWrap, int seed ) {

        int xMask = ( xWrap - 1 ) & 255;
        int yMask = ( yWrap - 1 ) & 255;
        int zMask = ( zWrap - 1 ) & 255;

        int px = fastFloor( x );
        int py = fastFloor( y );
        int pz = fastFloor( z );

        int x0 = px & xMask;
        int x1 = ( px + 1 ) & xMask;
        int y0 = py & yMask;
        int y1 = ( py + 1 ) & yMask;
        int z0 = pz & zMask;
        int z1 = ( pz + 1 ) & zMask;

        x -= px;
        y -= py;
        z -= pz;

        double u = ease( x );
        double v = ease( y );
        double w = ease( z );

        int r0 = RAND_TAB[x0 + seed];
        int r1 = RAND_TAB[x1 + seed];

        int r00 = RAND_TAB[r0 + y0];
        int r01 = RAND_TAB[r0 + y1];
        int r10 = RAND_TAB[r1 + y0];
        int r11 = RAND_TAB[r1 + y1];

        double n000 = grad( RAND_TAB_GRAD_IDX[r00 + z0], x, y, z );
        double n001 = grad( RAND_TAB_GRAD_IDX[r00 + z1], x, y, z - 1 );
        double n010 = grad( RAND_TAB_GRAD_IDX[r01 + z0], x, y - 1, z );
        double n011 = grad( RAND_TAB_GRAD_IDX[r01 + z1], x, y - 1, z - 1 );
        double n100 = grad( RAND_TAB_GRAD_IDX[r10 + z0], x - 1, y, z );
        double n101 = grad( RAND_TAB_GRAD_IDX[r10 + z1], x - 1, y, z - 1 );
        double n110 = grad( RAND_TAB_GRAD_IDX[r11 + z0], x - 1, y - 1, z );
        double n111 = grad( RAND_TAB_GRAD_IDX[r11 + z1], x - 1, y - 1, z - 1 );

        double n00 = MathUtils.lerp( n000, n001, w );
        double n01 = MathUtils.lerp( n010, n011, w );
        double n10 = MathUtils.lerp( n100, n101, w );
        double n11 = MathUtils.lerp( n110, n111, w );

        double n0 = MathUtils.lerp( n00, n01, v );
        double n1 = MathUtils.lerp( n10, n11, v );

        return MathUtils.lerp( n0, n1, u );

    }

    private static double noiseWrapNonPow2Internal( double x, double y, double z, int xWrap, int yWrap, int zWrap, int seed ) {

        int px = fastFloor( x );
        int py = fastFloor( y );
        int pz = fastFloor( z );

        int xWrap2 = xWrap != 0 ? xWrap : 256;
        int yWrap2 = yWrap != 0 ? yWrap : 256;
        int zWrap2 = zWrap != 0 ? zWrap : 256;

        int x0 = px % xWrap2;
        int y0 = py % yWrap2;
        int z0 = pz % zWrap2;

        if ( x0 < 0 ) x0 += xWrap2;
        if ( y0 < 0 ) y0 += yWrap2;
        if ( z0 < 0 ) z0 += zWrap2;

        int x1 = ( x0 + 1 ) % xWrap2;
        int y1 = ( y0 + 1 ) % yWrap2;
        int z1 = ( z0 + 1 ) % zWrap2;

        x -= px;
        y -= py;
        z -= pz;

        double u = ease( x );
        double v = ease( y );
        double w = ease( z );

        int r0 = RAND_TAB[x0];
        r0 = RAND_TAB[r0 + seed];
        int r1 = RAND_TAB[x1];
        r1 = RAND_TAB[r1 + seed];

        int r00 = RAND_TAB[r0 + y0];
        int r01 = RAND_TAB[r0 + y1];
        int r10 = RAND_TAB[r1 + y0];
        int r11 = RAND_TAB[r1 + y1];

        double n000 = grad( RAND_TAB_GRAD_IDX[r00 + z0], x, y, z );
        double n001 = grad( RAND_TAB_GRAD_IDX[r00 + z1], x, y, z - 1 );
        double n010 = grad( RAND_TAB_GRAD_IDX[r01 + z0], x, y - 1, z );
        double n011 = grad( RAND_TAB_GRAD_IDX[r01 + z1], x, y - 1, z - 1 );
        double n100 = grad( RAND_TAB_GRAD_IDX[r10 + z0], x - 1, y, z );
        double n101 = grad( RAND_TAB_GRAD_IDX[r10 + z1], x - 1, y, z - 1 );
        double n110 = grad( RAND_TAB_GRAD_IDX[r11 + z0], x - 1, y - 1, z );
        double n111 = grad( RAND_TAB_GRAD_IDX[r11 + z1], x - 1, y - 1, z - 1 );

        double n00 = MathUtils.lerp( n000, n001, w );
        double n01 = MathUtils.lerp( n010, n011, w );
        double n10 = MathUtils.lerp( n100, n101, w );
        double n11 = MathUtils.lerp( n110, n111, w );

        double n0 = MathUtils.lerp( n00, n01, v );
        double n1 = MathUtils.lerp( n10, n11, v );

        return MathUtils.lerp( n0, n1, u );

    }

    // Perlin's quintic ease curve: 6t^5 - 15t^4 + 10t^3
    private static double ease( double a ) {
        return ( ( a * 6 - 15 ) * a + 10 ) * a * a * a;
    }

    // floor as int, since a simple (int) cast truncates towards zero instead of down
    private static int fastFloor( double a ) {
        int ai = (int) a;
        return a < ai ? ai - 1 : ai;
    }

    private static double grad( int gradIdx, double x, double y, double z ) {
        double[] g = GRADIENT_BASIS[gradIdx];
        return g[0] * x + g[1] * y + g[2] * z;
    }

    private static int[] duplicate( int[] base ) {
        int[] result = new int[base.length * 2];
        System.arraycopy( base, 0, result, 0, base.length );
        System.arraycopy( base, 0, result, base.length, base.length );
        return result;
    }

}
