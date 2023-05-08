<template>
  <el-tabs
    class="el-tabs"
    v-model="editableTabsValue"
    type="card"
    closable
    @tab-remove="removeTab"
    @tab-click="clickTab"
  >
    <el-tab-pane
      class="tab-pane"
      v-for="item in editableTabs"
      :key="item.name"
      :label="item.title"
      :name="item.name"
    >
    </el-tab-pane>
  </el-tabs>
</template>

<script>
export default {
  name: "tabs.vue",
  data() {
    return {
    };
  },
  computed: {
    editableTabs: {
      get() {
        return this.$store.state.Tabs.editableTabs;
      },
      set(val) {
        this.$store.state.Tabs.editableTabs = val;
      },
    },
    editableTabsValue: {
      get() {
        return this.$store.state.Tabs.editableTabsValue;
      },
      set(val) {
        this.$store.state.Tabs.editableTabsValue = val;
      },
    },
  },
  methods: {
    removeTab(targetName) {
      let tabs = this.editableTabs;
      let activeName = this.editableTabsValue;

      if (targetName === "Index") {
        return;
      }

      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }
      this.editableTabsValue = activeName;
      this.editableTabs = tabs.filter((tab) => tab.name != targetName);

      this.$router.push({ name: activeName });
    },
    clickTab(target) {
      this.$router.push({ name: target.name });
    },
  },
};
</script>

<style>
</style>