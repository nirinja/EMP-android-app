package com.example.receptiapp.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RecipeDao_Impl implements RecipeDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Recipe> __insertionAdapterOfRecipe;

  private final EntityDeletionOrUpdateAdapter<Recipe> __deletionAdapterOfRecipe;

  private final EntityDeletionOrUpdateAdapter<Recipe> __updateAdapterOfRecipe;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRecipeByName;

  public RecipeDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecipe = new EntityInsertionAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `recipes` (`recipeId`,`name`,`ingredients`,`instructions`,`prepTime`,`cookTime`,`difficulty`,`image`,`mealType`,`categoryId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Recipe entity) {
        statement.bindLong(1, entity.getRecipeId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getIngredients() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIngredients());
        }
        if (entity.getInstructions() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getInstructions());
        }
        statement.bindLong(5, entity.getPrepTime());
        statement.bindLong(6, entity.getCookTime());
        if (entity.getDifficulty() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDifficulty());
        }
        if (entity.getImage() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImage());
        }
        if (entity.getMealType() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getMealType());
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getCategoryId());
        }
      }
    };
    this.__deletionAdapterOfRecipe = new EntityDeletionOrUpdateAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `recipes` WHERE `recipeId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Recipe entity) {
        statement.bindLong(1, entity.getRecipeId());
      }
    };
    this.__updateAdapterOfRecipe = new EntityDeletionOrUpdateAdapter<Recipe>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `recipes` SET `recipeId` = ?,`name` = ?,`ingredients` = ?,`instructions` = ?,`prepTime` = ?,`cookTime` = ?,`difficulty` = ?,`image` = ?,`mealType` = ?,`categoryId` = ? WHERE `recipeId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Recipe entity) {
        statement.bindLong(1, entity.getRecipeId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getIngredients() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIngredients());
        }
        if (entity.getInstructions() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getInstructions());
        }
        statement.bindLong(5, entity.getPrepTime());
        statement.bindLong(6, entity.getCookTime());
        if (entity.getDifficulty() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDifficulty());
        }
        if (entity.getImage() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getImage());
        }
        if (entity.getMealType() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getMealType());
        }
        if (entity.getCategoryId() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getCategoryId());
        }
        statement.bindLong(11, entity.getRecipeId());
      }
    };
    this.__preparedStmtOfDeleteRecipeByName = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM recipes WHERE name = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertRecipe(final Recipe recipe, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRecipe.insertAndReturnId(recipe);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecipe(final Recipe recipe, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRecipe.handle(recipe);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateRecipe(final Recipe recipe, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRecipe.handle(recipe);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRecipeByName(final String name,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRecipeByName.acquire();
        int _argIndex = 1;
        if (name == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, name);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteRecipeByName.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecipeById(final int id, final Continuation<? super Recipe> $completion) {
    final String _sql = "SELECT * FROM recipes WHERE recipeId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Recipe>() {
      @Override
      @Nullable
      public Recipe call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRecipeId = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrepTime = CursorUtil.getColumnIndexOrThrow(_cursor, "prepTime");
          final int _cursorIndexOfCookTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cookTime");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final Recipe _result;
          if (_cursor.moveToFirst()) {
            final int _tmpRecipeId;
            _tmpRecipeId = _cursor.getInt(_cursorIndexOfRecipeId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIngredients;
            if (_cursor.isNull(_cursorIndexOfIngredients)) {
              _tmpIngredients = null;
            } else {
              _tmpIngredients = _cursor.getString(_cursorIndexOfIngredients);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final int _tmpPrepTime;
            _tmpPrepTime = _cursor.getInt(_cursorIndexOfPrepTime);
            final int _tmpCookTime;
            _tmpCookTime = _cursor.getInt(_cursorIndexOfCookTime);
            final String _tmpDifficulty;
            if (_cursor.isNull(_cursorIndexOfDifficulty)) {
              _tmpDifficulty = null;
            } else {
              _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            }
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpMealType;
            if (_cursor.isNull(_cursorIndexOfMealType)) {
              _tmpMealType = null;
            } else {
              _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            }
            final Integer _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            }
            _result = new Recipe(_tmpRecipeId,_tmpName,_tmpIngredients,_tmpInstructions,_tmpPrepTime,_tmpCookTime,_tmpDifficulty,_tmpImage,_tmpMealType,_tmpCategoryId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllRecipes(final Continuation<? super List<Recipe>> $completion) {
    final String _sql = "SELECT * FROM recipes";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Recipe>>() {
      @Override
      @NonNull
      public List<Recipe> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRecipeId = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrepTime = CursorUtil.getColumnIndexOrThrow(_cursor, "prepTime");
          final int _cursorIndexOfCookTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cookTime");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final List<Recipe> _result = new ArrayList<Recipe>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Recipe _item;
            final int _tmpRecipeId;
            _tmpRecipeId = _cursor.getInt(_cursorIndexOfRecipeId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIngredients;
            if (_cursor.isNull(_cursorIndexOfIngredients)) {
              _tmpIngredients = null;
            } else {
              _tmpIngredients = _cursor.getString(_cursorIndexOfIngredients);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final int _tmpPrepTime;
            _tmpPrepTime = _cursor.getInt(_cursorIndexOfPrepTime);
            final int _tmpCookTime;
            _tmpCookTime = _cursor.getInt(_cursorIndexOfCookTime);
            final String _tmpDifficulty;
            if (_cursor.isNull(_cursorIndexOfDifficulty)) {
              _tmpDifficulty = null;
            } else {
              _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            }
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpMealType;
            if (_cursor.isNull(_cursorIndexOfMealType)) {
              _tmpMealType = null;
            } else {
              _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            }
            final Integer _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            }
            _item = new Recipe(_tmpRecipeId,_tmpName,_tmpIngredients,_tmpInstructions,_tmpPrepTime,_tmpCookTime,_tmpDifficulty,_tmpImage,_tmpMealType,_tmpCategoryId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecipeByName(final String name, final Continuation<? super Recipe> $completion) {
    final String _sql = "SELECT * FROM recipes WHERE name = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Recipe>() {
      @Override
      @Nullable
      public Recipe call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRecipeId = CursorUtil.getColumnIndexOrThrow(_cursor, "recipeId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfIngredients = CursorUtil.getColumnIndexOrThrow(_cursor, "ingredients");
          final int _cursorIndexOfInstructions = CursorUtil.getColumnIndexOrThrow(_cursor, "instructions");
          final int _cursorIndexOfPrepTime = CursorUtil.getColumnIndexOrThrow(_cursor, "prepTime");
          final int _cursorIndexOfCookTime = CursorUtil.getColumnIndexOrThrow(_cursor, "cookTime");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final Recipe _result;
          if (_cursor.moveToFirst()) {
            final int _tmpRecipeId;
            _tmpRecipeId = _cursor.getInt(_cursorIndexOfRecipeId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpIngredients;
            if (_cursor.isNull(_cursorIndexOfIngredients)) {
              _tmpIngredients = null;
            } else {
              _tmpIngredients = _cursor.getString(_cursorIndexOfIngredients);
            }
            final String _tmpInstructions;
            if (_cursor.isNull(_cursorIndexOfInstructions)) {
              _tmpInstructions = null;
            } else {
              _tmpInstructions = _cursor.getString(_cursorIndexOfInstructions);
            }
            final int _tmpPrepTime;
            _tmpPrepTime = _cursor.getInt(_cursorIndexOfPrepTime);
            final int _tmpCookTime;
            _tmpCookTime = _cursor.getInt(_cursorIndexOfCookTime);
            final String _tmpDifficulty;
            if (_cursor.isNull(_cursorIndexOfDifficulty)) {
              _tmpDifficulty = null;
            } else {
              _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            }
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpMealType;
            if (_cursor.isNull(_cursorIndexOfMealType)) {
              _tmpMealType = null;
            } else {
              _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            }
            final Integer _tmpCategoryId;
            if (_cursor.isNull(_cursorIndexOfCategoryId)) {
              _tmpCategoryId = null;
            } else {
              _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            }
            _result = new Recipe(_tmpRecipeId,_tmpName,_tmpIngredients,_tmpInstructions,_tmpPrepTime,_tmpCookTime,_tmpDifficulty,_tmpImage,_tmpMealType,_tmpCategoryId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
