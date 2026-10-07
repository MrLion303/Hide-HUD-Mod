package com.mrlion303.hidehud;

public final class HideHudState {
    private HideHudState() {}

    public static boolean hidden;
    public static boolean hideHand;
    public static long transitionStart;
    public static long transitionDurationMs;
    public static boolean transitionHiding;

    public static void set(boolean hide, int seconds, boolean hand) {
        hidden = hide;
        hideHand = hide && hand;
        transitionHiding = hide;
        transitionStart = System.currentTimeMillis();
        transitionDurationMs = Math.max(0L, seconds * 1000L);
    }

    public static float progress() {
        if (transitionDurationMs <= 0L) return hidden ? 1f : 0f;
        float p = Math.min(1f,
            (System.currentTimeMillis() - transitionStart) / (float) transitionDurationMs);
        return transitionHiding ? p : 1f - p;
    }

    public static float visibilityAlpha() {
        if (transitionDurationMs <= 0L) return hidden ? 0f : 1f;
        return transitionHiding ? 1f - progress() : progress();
    }

    public static boolean fullyHidden() {
        return hidden && visibilityAlpha() <= 0f;
    }
}
