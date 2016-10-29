package com.shellever.sqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shellever.bean.User;
import com.shellever.sqliker.SQLiker;

import java.util.List;
import java.util.Map;
import java.util.Set;

// 10/28/2016  -  使用反射机制完成数据库的增删改查功能
// 10/28/2016  -  优化代码，减少冗余，提高性能 ---
public class MainActivity extends AppCompatActivity {

    private SQLiker<User> mSQLiker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSQLiker = new SQLiker<>(this, User.class);         // 初始化SQLiker
    }

    // 1. 先添加数据到数据库
    // 只有当手动卸载程序时，才会将程序中的数据库删除掉
    public void testAddClick(View view) {
        User user = new User("Curry", "Male", 28, 1000.0f);
        mSQLiker.insert(user);                              // 插入数据
        Toast.makeText(this, "Add: " + user.toString(), Toast.LENGTH_SHORT).show();

        user = new User("Hyper", "Male", 23, 11.19f);
        mSQLiker.insert(user);
        Toast.makeText(this, "Add: " + user.toString(), Toast.LENGTH_SHORT).show();
    }

    // 2. 查找数据库中的所有数据并输出其自动生成的id
    public void testFindAllClick(View view) {
//        List<User> mUserList = mSQLiker.findAll();
//        for(User u: mUserList){
//            Toast.makeText(this, u.toString(), Toast.LENGTH_SHORT).show();
//        }
        Map<Integer, User> mUserMap = mSQLiker.queryAll();  // 获取所有数据
        if(mUserMap == null){
            Toast.makeText(this, "UserMap is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "FindAll", Toast.LENGTH_SHORT).show();
        Set<Integer> set = mUserMap.keySet();
        for(int i: set){
            Toast.makeText(this, i + " - " + mUserMap.get(i).toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // 3. 根据名字或者其他条件来查找数据
    public void testFindByNameClick(View view) {
        Map<Integer, User> mUserMap = mSQLiker.query("name=Curry");     // or age>25
        if(mUserMap == null){
            Toast.makeText(this, "UserMap is null", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "FindByName: name=Curry", Toast.LENGTH_SHORT).show();
        Set<Integer> set = mUserMap.keySet();
        for(int i: set){
            Toast.makeText(this, i + " - " + mUserMap.get(i).toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // 4. 根据名字或者其他条件来更新数据
    public void testUpdateByNameClick(View view) {
        User user = new User("Hyper", "Male", 23, 12.19f);
        Toast.makeText(this, "UpdateByName: " + user.toString(), Toast.LENGTH_SHORT).show();
        mSQLiker.update(user, "name=Hyper");            // 更新数据
    }

    // 5. 根据名字或者其他条件来删除数据
    public void testDeleteByNameClick(View view) {
        Toast.makeText(this, "DeleteByName: name=Curry", Toast.LENGTH_SHORT).show();
        mSQLiker.delete("name=Curry");                  // 删除数据
    }

    public void testGetCountClick(View view) {
        int count = mSQLiker.getCount();
        Toast.makeText(this, "GetCount: " + count, Toast.LENGTH_SHORT).show();
    }
}
