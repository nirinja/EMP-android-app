package com.example.receptiapp;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a\u0016\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\u0007\u001a&\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\u0007\u001aN\u0010\r\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\u0006\u0010\u0010\u001a\u00020\u000b2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00050\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\u0012H\u0007\u001a*\u0010\u0014\u001a\u00020\u00052\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\u00122\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\u0007\u001a\u001b\u0010\u0016\u001a\u00020\u00052\u0011\u0010\u0017\u001a\r\u0012\u0004\u0012\u00020\u00050\u0007\u00a2\u0006\u0002\b\u0018H\u0007\u001a\u001e\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001a\u001a\u00020\u001b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00050\u0007H\u0007\u001a\u0014\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000fH\u0086@\u00a2\u0006\u0002\u0010\u001d\u001a\u0014\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000fH\u0086@\u00a2\u0006\u0002\u0010\u001d\u001a\u0014\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u001b0\u000f2\u0006\u0010 \u001a\u00020!\u001a\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\u0006\u0010#\u001a\u00020\u0001\u001a\u0010\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f*\u00020!\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"filtri", "", "saved", "search", "BottomMenuBar", "", "onSavedClick", "Lkotlin/Function0;", "FilterChip", "label", "isSelected", "", "onClick", "FilterDropdown", "options", "", "dropdownExpanded", "onDropdownToggle", "Lkotlin/Function1;", "onOptionSelected", "MainScreen", "onSearchClick", "ReceptiAppTheme", "content", "Landroidx/compose/runtime/Composable;", "RecipeItem", "recipe", "Lcom/example/receptiapp/Recipe;", "fetchRecipesData", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadRecipesFromDatabase", "parseRecipes", "jsonArray", "Lorg/json/JSONArray;", "toListR", "value", "toList", "app_debug"})
public final class MainActivityKt {
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String saved = "";
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String search = "";
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String filtri = "";
    
    @androidx.compose.runtime.Composable()
    public static final void MainScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSearchClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSavedClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void FilterDropdown(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> options, boolean dropdownExpanded, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onDropdownToggle, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onOptionSelected) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void FilterChip(@org.jetbrains.annotations.NotNull()
    java.lang.String label, boolean isSelected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void BottomMenuBar(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onSavedClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void RecipeItem(@org.jetbrains.annotations.NotNull()
    com.example.receptiapp.Recipe recipe, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void ReceptiAppTheme(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.Object loadRecipesFromDatabase(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.receptiapp.Recipe>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<java.lang.String> toListR(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final java.lang.Object fetchRecipesData(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.receptiapp.Recipe>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<com.example.receptiapp.Recipe> parseRecipes(@org.jetbrains.annotations.NotNull()
    org.json.JSONArray jsonArray) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<java.lang.String> toList(@org.jetbrains.annotations.NotNull()
    org.json.JSONArray $this$toList) {
        return null;
    }
}