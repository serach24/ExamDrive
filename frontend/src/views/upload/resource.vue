<template>
  <div>
    <el-select v-model="value1" @change="setPath" placeholder="Subject">
      <el-option
        v-for="item in options1"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>

    <el-select
      v-model="value2"
      style="margin-left: 20px;"
      placeholder="Repository"
      @change="setPath">
      <el-option
        v-for="item in options2"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
    <el-input v-model="inputvalue" 
      placeholder="RelativePath" 
      @change="setPath"
      @focus="tip"
      :disabled="disabled"
      style="margin-left:20px; width:300px">
    </el-input>
  </div>
</template>

<script>
import { METHODS } from 'http';
  export default {
    data() {
      return {
        options1: [{
          value: 'Biology',
          label: 'Biology'
        }, {
          value: 'Chemistry',
          label: 'Chemistry'
        }, {
          value: 'Physics',
          label: 'Physics'
        }, {
          value: 'Maths-Further-HL',
          label: 'Maths Further HL'
        }, {
          value: 'Maths-HL',
          label: 'Maths HL'
        }, {
          value: 'Maths SL',
          label: 'Maths SL'
        }, {
          value: 'Geography',
          label: 'Geography'
        }],

        options2:[{
          value: 'Textbooks',
          label: 'Textbooks'
        }, {
          value: 'Question Bank',
          label: 'Question Bank'
        }, {
          value: 'Past Exams',
          label: 'Past Exams'
        }, {
          value: 'Notes',
          label: 'Notes'
        }, {
          value: 'Guide',
          label: 'Guide'
        }, {
          value: 'Essay Examples',
          label: 'Essay Examples'
        }],
        value1: '',
        value2: '',
        inputvalue: '',
        disabled: true
      }
    },
    methods: {
      tip(){
        const h = this.$createElement;
        this.$notify({
          title: 'Tips',
          message: h('i', { style: 'color: teal'}, 'If you want to upload to a specific folder, you can fill in the relative path. Example: Solutions/2019/ExamA. Make sure the path exists.' )
        });
      },
      setPath() {
        if (this.value1 && this.value2){
          this.disabled = false
        }
        this.$emit('transfer', this.value1, this.value2, this.inputvalue)
      }
    }
  }
</script>