import request from '@/utils/request'
import axios from "axios"

export function download(data) {
  return axios({
    url: '/dev-api/ibsource/filemanager/download',
    method: 'post',
    data,
    responseType: "blob"
  })
}

export function getList(data) {
  return request({
    url: '/ibsource/filemanager/list',
    method: 'post',
    data
  })
}

export function newFolder(data) {
  return request({
    url: '/ibsource/filemanager/createFolder',
    method: 'post',
    data
  })
}

export function deleteFile(data) {
  return request({
    url: '/ibsource/filemanager/delete',
    method: 'post',
    data
  })
}