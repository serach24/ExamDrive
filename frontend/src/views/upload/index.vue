<template>
  <div>
    <resource class="resource-setting" @transfer = "getPath"></resource>
    <uploader v-if="this.path"
      :options="options"
      :file-status-text="statusText"
      class="uploader-example"
      ref="uploader"
      @file-complete="fileComplete"
      @complete="complete"
    ></uploader>
  </div>
</template>

<script>
import axios from 'axios'
import qs from 'qs'
import Resource from './resource.vue'
export default {
  components: {
    Resource
  },
  data() {
    return {
      path: '',
      options: {
        target: '/dev-api/ibsource/upload/chunk',
        testChunks: false,
        simultaneousUploads: 1,
        chunkSize: 10 * 1024 * 1024,
        // checkChunkUploadedByResponse: function (chunk, message) {
        //   let objMessage = {}
        //   try {
        //     objMessage = JSON.parse(message)
        //   } catch (e) {
        //   }
        //   // fake response
        //   // objMessage.uploaded_chunks = [2, 3, 4, 5, 6, 8, 10, 11, 12, 13, 17, 20, 21]
        //   // check the chunk is uploaded
        //   return (objMessage.uploaded_chunks || []).indexOf(chunk.offset + 1) >= 0
        // }
      },
      attrs: {
        accept: 'image/*'
      },
      statusText: {
        success: 'Success',
        error: 'Error',
        uploading: 'Uploading',
        paused: 'Paused',
        waiting: 'Waiting'
      }
    }
  },
   mounted() {
    this.$nextTick(() => {
      window.uploader = this.$refs.uploader.uploader
    })
  },
  methods: {
    getPath(value1, value2, value3){
      if (value1 && value2) this.path=value1+'/'+value2
      if (value3) this.path+='/' + value3;
    },
    complete() {
      console.log('complete', arguments)
    },
    fileComplete() {
      console.log('file complete', arguments)
      const file = arguments[0].file;
      axios.post('/dev-api/ibsource/upload/mergeFile', qs.stringify({
        filename: file.name,
        identifier: arguments[0].uniqueIdentifier,
        path: this.path,
        totalSize: file.size,
        type: file.type
      })).then(function (response) {
        console.log(response)
      }).catch(function (error) {
        console.log(error)
      })
    }
  }
}
</script>

<style>
.resource-setting {
  width: 1000px;
  padding: 15px;
  margin: 20px 20px 0;
  font-size: 12px;
}

.uploader-example {
  width: 1000px;
  padding: 15px;
  margin: 0 20px 0;
  font-size: 12px;
  /* box-shadow: 0 0 10px rgba(62, 146, 203, 0.4); */
}

.uploader-example .uploader-btn {
  margin-right: 4px;
}

.uploader-example .uploader-list {
  max-height: 440px;
  overflow: auto;
  overflow-x: hidden;
  overflow-y: auto;
}
</style>
