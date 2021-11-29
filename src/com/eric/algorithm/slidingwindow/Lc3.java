package com.eric.algorithm.slidingwindow;

import java.util.HashMap;
import java.util.Map;

/**
 * 3. 无重复字符的最长子串
 * https://leetcode-cn.com/problems/longest-substring-without-repeating-characters/
 *
 * @Classname Solution
 * @Date 2021/11/29 12:01 上午
 * @Created by eric
 *
 */
class Lc3 {
    /**
     * ... <--s[right]] 向左扩展
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        int maxLength = 0;
        Map<Character, Integer> charFrequency = new HashMap<>();

        // 以s[right]为终点的最大不重复子串长度
        for (int right = 0, left = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            // 注意map存在key = 1.Key不为空 2.Map[key] != 0
            while (charFrequency.containsKey(rightChar) && charFrequency.get(rightChar) != 0) {
                char leftChar = s.charAt(left);
                charFrequency.put(leftChar, charFrequency.get(leftChar) - 1);
                left++;
            }
            charFrequency.put(rightChar, charFrequency.getOrDefault(rightChar, 0) + 1);
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }

    /**
     * [s[left] -> ... 向右扩展
     * @param s
     * @return
     */
    public static int lengthOfLongestSubstring2(String s) {
        int maxLength = 0;
        Map<Character, Integer> charFrequency = new HashMap<>();

        // 以s[left]为起点的最大不重复子串长度
        for (int left = 0, right = 0; left < s.length(); left++) {
            // 不包含s[right]
            while (right < s.length() && !(charFrequency.containsKey(s.charAt(right)) && charFrequency.get(s.charAt(right)) != 0)) {
                char rightChar = s.charAt(right);
                charFrequency.put(rightChar, charFrequency.getOrDefault(rightChar, 0) + 1);
                right++;
            }

            maxLength = Math.max(maxLength, right - left);

            char leftChar = s.charAt(left);
            charFrequency.put(leftChar, charFrequency.get(leftChar) - 1);
        }

        return maxLength;
    }

    /**
     * 还可以进一步优化
     * @param args
     */

    public static void main(String[] args) {
        int a = lengthOfLongestSubstring2("abcasdasdafasgdabc");
        System.out.println(a);
    }
}