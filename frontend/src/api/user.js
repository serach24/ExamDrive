import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/ibsource/login',
    method: 'post',
    data
  })
}

export function signup(data) {
  return request({
    url: '/ibsource/signup',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/ibsource/signup/current',
    method: 'get',
    params: { token }
  })
}

/*
export function logout() {
  return request({
    url: '/ibsource/user/logout',
    method: 'post'
  })
}
*/
