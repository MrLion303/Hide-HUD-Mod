package com.mrlion303.hidehud;

public final class HideHudState {
    private HideHudState() {}
    public static boolean hidden;
    public static boolean hideHand;
    public static long transitionStart;
    public static long transitionDurationMs;
    public static boolean transitionHiding;

    public static void set(boolean hide, int seconds, boolean hand) {
        hideHand = hide ? hand : false;
        transitionHiding = hide;
        transitionStart = System.currentTimeMillis();
        transitionDurationMs = Math.max(0L, seconds * 1000L);
        if (transitionDurationMs == 0L) {
            hidden = hide;
        }
    }

    public static float progress() {
        if (transitionDurationMs <= 0L) return hidden ? 1f : 0f;
        float p = Math.min(1f, (System.currentTimeMillis() - transitionStart) / (float) transitionDurationMs);
        if (p >= 1f) hidden = transitionHiding;
        return transitionHiding ? p : 1f - p;
    }

    public static boolean fullyHidden() {
        return hidden && transitionDurationMs == 0L || (transitionDurationMs > 0L && transitionHiding && progress() >= 1f);
    }
}
