package com.example.receptiapp.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0018\u0010\u0012\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0016"}, d2 = {"Lcom/example/receptiapp/data/RecipeDao;", "", "deleteRecipe", "", "recipe", "Lcom/example/receptiapp/data/Recipe;", "(Lcom/example/receptiapp/data/Recipe;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRecipeByName", "name", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllRecipes", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecipeById", "id", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecipeByName", "insertRecipe", "", "updateRecipe", "app_debug"})
@androidx.room.Dao()
public abstract interface RecipeDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertRecipe(@org.jetbrains.annotations.NotNull()
    com.example.receptiapp.data.Recipe recipe, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateRecipe(@org.jetbrains.annotations.NotNull()
    com.example.receptiapp.data.Recipe recipe, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteRecipe(@org.jetbrains.annotations.NotNull()
    com.example.receptiapp.data.Recipe recipe, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM recipes WHERE recipeId = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecipeById(int id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.receptiapp.data.Recipe> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM recipes")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllRecipes(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.receptiapp.data.Recipe>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM recipes WHERE name = :name LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecipeByName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.receptiapp.data.Recipe> $completion);
    
    @androidx.room.Query(value = "DELETE FROM recipes WHERE name = :name")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteRecipeByName(@org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}