package com.dirror.music.ui.playlist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dirror.music.music.local.MyFavorite
import com.dirror.music.music.netease.Playlist
import com.dirror.music.music.netease.PlaylistUtil
import com.dirror.music.music.standard.data.StandardSongData
import com.dirror.music.util.runOnMainThread

const val TAG_LOCAL_MY_FAVORITE = 0
const val TAG_NETEASE = 1

class SongPlaylistViewModel: ViewModel() {

    companion object {

    }

    var tag = MutableLiveData(TAG_NETEASE)

    var playlistTitle = MutableLiveData("")

    var playlistDescription =MutableLiveData("")

    var playlistId = MutableLiveData("")

    var playlistUrl = MutableLiveData("")

    var songList = MutableLiveData(ArrayList<StandardSongData>())

    fun update(context: Context) {
        when (tag.value) {
            TAG_NETEASE -> {
                Playlist.getPlaylist(context, playlistId.value?.toLong() ?: 0L, {
                    setSongList(it)
                }, {

                })
            }
            TAG_LOCAL_MY_FAVORITE -> {
                MyFavorite.read {
                    setSongList(it)
                }
            }
        }
    }

    fun updateInfo(context: Context) {
        when (tag.value) {
            TAG_NETEASE -> {
                PlaylistUtil.getPlaylistInfo(context, playlistId.value?.toLong() ?: 0L) {
                    runOnMainThread {
                        playlistUrl.value = it.coverImgUrl ?: ""
                        playlistTitle.value = it.name ?: ""
                        playlistDescription.value = it.description ?: ""
                    }
                }
            }
            TAG_LOCAL_MY_FAVORITE -> {
                playlistTitle.value = "本地我喜欢"
                playlistDescription.value = "收藏你喜欢的本地、网易云、QQ 音乐等歌曲"
                songList.value?.let {
                    if (it.size > 0) {
                        playlistUrl.value = songList.value?.get(0)?.imageUrl ?: ""
                    }
                }
            }
        }
    }

    private fun setSongList(list: ArrayList<StandardSongData>) {
        runOnMainThread {
            songList.value = list
        }
    }

    var navigationBarHeight = MutableLiveData<Int>()

}