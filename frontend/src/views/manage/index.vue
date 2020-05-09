<template>
  <div class="app-container">
    <el-input v-model="filterText" placeholder="Filter keyword" style="margin-bottom:30px;"/>
    <el-tree
      ref="tree"
      :data="data"
      :props="defaultProps"
      :filter-node-method="filterNode"
      :expand-on-click-node="false"
      class="filter-tree"
    >
      <!-- @node-click="handleNodeClick" -->
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }} </span>
        <span>  
          <el-button v-if="data.type=='dir' && node.level!=0 && node.level!=1" type="text" @click="() => newFolder(node, data)">NewFolder</el-button>
          <el-button v-else type="text">&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;</el-button>
          <el-button v-if="data.type!='dir'" type="text" @click="() => download(node, data)">Download</el-button>
          <el-button v-else type="text">&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;</el-button>
          <el-button v-if="node.level!=0 && node.level!=1 && node.level!=2" style= "color: red" type="text" @click="() => remove(node, data)">Delete</el-button>
          <el-button v-else type="text">&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;</el-button>
        </span>
      </span>
    </el-tree>
  </div>
</template>

<script>
import { download, getList, deleteFile, newFolder } from "@/api/file";
import { Promise } from "q";
let id = 0;
export default {
  data() {
    const data = [];
    return {
      filterText: "",
      data,
      defaultProps: {
        children: "children",
        label: "label",
        path: "",
        type: "type"
      }
    };
  },
  created() {
    this.init();
  },

  watch: {
    filterText(val) {
      this.$refs.tree2.filter(val);
    }
  },

  methods: {
    async init() {
      const { result } = await getList({ path: "" });
      console.log(result);
      this.data = this.getNodeTree(result, this.data);
    },
    getNodeTree(tree, data) {
      for (let i = 0; i < tree.length; i++) {
        var newChildren = [];
        if (tree[i]["children"]) {
          newChildren = this.getNodeTree(tree[i]["children"], newChildren);
        }
        data.push({
          id: id++,
          label: tree[i]["name"],
          children: newChildren,
          path: tree[i]["path"],
          type: tree[i]["type"]
        });
      }
      return data;
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },

    append(data) {
      const newChild = { id: id++, label: filename, children: [] };
      if (!data.children) {
        this.$set(data, "children", []);
      }
      data.children.push(newChild);
    },

    download(node, data) {
      const { label, path, type } = data;
      if (type == "dir") return;
      const form = { filename: label, path: path };
      download(form).then(res => {
        const {data1} = res;
        const blob = new Blob([data1]);
        if ("download" in document.createElement("a")) {
          // not IE
          const elink = document.createElement("a");
          elink.download = label;
          elink.style.display = "none";
          elink.href = URL.createObjectURL(blob);
          document.body.appendChild(elink);
          elink.click();
          URL.revokeObjectURL(elink.href);
          document.body.removeChild(elink);
        } else {
          // IE10
          navigator.msSaveBlob(blob, label);
        }
      });
    },
    newFolder(node, data) {
      this.$prompt('Please Enter the Folder Name', 'New Folder',{
        confirmButtonText: 'Submit',
        cancelButtonText: 'Cancel',
      }).then(({ value }) => {
        const {path, label} = data
        let folderPath = path+'/'+label+'/'+value
        newFolder({
          path: folderPath
        }).then((res)=>{
          const {message} = res
          if (message=="Create Success"){
            this.$message({
            type: 'success',
            message: message+", please reload the page"
          })
          } else {
            this.$message({
            type: 'warning',
            message: message
            })
          }
        })
      })
    },
    remove(node, data) {
      const { label, path, type } = data;
      if (path=="dir") var notif=label+" is a directory, all files included will be permanently deleted, are you sure?"
      else var notif=label+ " will be permanently deleted, are you sure?"
      this.$confirm(notif, 'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
      }).then(()=>{
        const res = deleteFile({
          path: path,
          filename: label,
          type: type
        }).then(() => {
          this.$message({
            type: 'success',
            message: 'Delete Success, please reload the page'
          });
        })
      })
    }
  }
};
</script>

<style>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 20px;
  }

</style>