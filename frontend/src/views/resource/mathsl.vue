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
      @node-click="handleNodeClick"
    />
  </div>
</template>

<script>
import { download, getList } from "@/api/file";
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
        path: "biology",
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
      const { result } = await getList({ path: "biology" });
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

    handleNodeClick(value) {
      const { label, path, type } = value;
      if (type == "dir") return;
      const data = { filename: label, path: path };
      download(data).then(res => {
        const {data} = res;
        const blob = new Blob([data]);
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
    }
  }
};
</script>