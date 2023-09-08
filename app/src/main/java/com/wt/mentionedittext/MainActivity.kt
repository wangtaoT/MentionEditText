package com.wt.mentionedittext

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.wt.mention.bean.MentionTopic
import com.wt.mention.bean.MentionUser
import com.wt.mention.edit.listener.EditDataListener
import com.wt.mentionedittext.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.etContent.editDataListener = object : EditDataListener {
            override fun onEditAddAt(str: String, start: Int, length: Int) {
                showUserDialog(str)
            }

            override fun onEditAddHashtag(start: Int) {
                showTopicDialog()
            }

            override fun onCloseEdit() {
                if (binding.rlDialog.isShown && binding.llUser.isShown) {
                    binding.rlDialog.visibility = View.GONE
                }
            }
        }

        binding.btnAddTopic.setOnClickListener {
            showTopicDialog()
        }
        binding.btnAddUser.setOnClickListener {
            binding.etContent.insertText("@")
        }

        binding.btnInsert.setOnClickListener {
            binding.etContent.setText("#国庆快乐 这是一个话题标签")
            binding.etContent.insertConvert(
                MentionTopic(
                    "11",
                    "国庆快乐",
                ),
                0,
                6
            )
        }


        binding.btnGet.setOnClickListener {
            val list = binding.etContent.formatResult.topicList
            if (list == null || list.size == 0) {
                binding.tvData.text = ""
            } else {
                val jsonArray = JSONArray()
                for (item in list) {
                    val jsonObject = JSONObject()
                    jsonObject.put("id", item.id)
                    jsonObject.put("name", item.name)
                    jsonObject.put("startIndex", item.fromIndex)
                    jsonObject.put("length", item.length)
                    jsonArray.put(jsonObject)
                }
                binding.tvData.text = jsonArray.toString()
            }
        }

        binding.btnGet2.setOnClickListener {
            val list = binding.etContent.formatResult.userList
            if (list == null || list.size == 0) {
                binding.tvData.text = ""
            } else {
                val jsonArray = JSONArray()
                for (item in list) {
                    val jsonObject = JSONObject()
                    jsonObject.put("id", item.id)
                    jsonObject.put("name", item.name)
                    jsonObject.put("startIndex", item.fromIndex)
                    jsonObject.put("length", item.length)
                    jsonArray.put(jsonObject)
                }
                binding.tvData.text = jsonArray.toString()
            }
        }
    }

    private fun showTopicDialog() {
        binding.rlDialog.visibility = View.VISIBLE
        binding.llTopic.visibility = View.VISIBLE
        binding.llUser.visibility = View.GONE
        binding.btnTopic1.setOnClickListener {
            binding.etContent.insert(MentionTopic("11", "国庆快乐"))
            binding.rlDialog.visibility = View.GONE
        }
        binding.btnTopic2.setOnClickListener {
            binding.etContent.insert(MentionTopic("22", "中秋快乐"))
            binding.rlDialog.visibility = View.GONE
        }
    }

    private fun showUserDialog(name: String) {
        binding.rlDialog.visibility = View.VISIBLE
        binding.llTopic.visibility = View.GONE
        binding.llUser.visibility = View.VISIBLE

        binding.tvUser1.text = "${name}111"
        binding.tvUser2.text = "${name}222"

        binding.llUser1.setOnClickListener {
            binding.etContent.insert(MentionUser("11", binding.tvUser1.text.toString()))
            binding.rlDialog.visibility = View.GONE
        }
        binding.llUser2.setOnClickListener {
            binding.etContent.insert(MentionUser("22", binding.tvUser2.text.toString()))
            binding.rlDialog.visibility = View.GONE
        }
    }
}