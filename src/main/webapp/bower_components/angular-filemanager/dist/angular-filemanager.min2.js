! function(e, r, n) {
    "use strict";
    r.module("FileManagerApp", ["pascalprecht.translate", "ngFileUpload"]), n(e.document).on("shown.bs.modal", ".modal", function() {
        e.setTimeout(function() {
            n("[autofocus]", this).focus()
        }.bind(this), 100)
    }), n(e.document).on("click", function() {
        n("#context-menu").hide()
    }), n(e.document).on("contextmenu", '.main-navigation .table-files tr.item-list:has("td"), .item-list', function(r) {
        var i = n("#context-menu");
        r.pageX >= e.innerWidth - i.width() && (r.pageX -= i.width()), r.pageY >= e.innerHeight - i.height() && (r.pageY -= i.height()), i.hide().css({
            left: r.pageX,
            top: r.pageY
        }).show(), r.preventDefault()
    }), Array.prototype.find || (Array.prototype.find = function(e) {
        if (null == this) throw new TypeError("Array.prototype.find called on null or undefined");
        if ("function" != typeof e) throw new TypeError("predicate must be a function");
        for (var r, n = Object(this), i = n.length >>> 0, a = arguments[1], t = 0; i > t; t++)
            if (r = n[t], e.call(a, r, t, n)) return r
    })
}(window, angular, jQuery),
function(e) {
    "use strict";
    var r = e.module("FileManagerApp");
    r.directive("angularFilemanager", ["$parse", "fileManagerConfig", function(e, r) {
        return {
            restrict: "EA",
            templateUrl: r.tplPath + "/main.html"
            
        }
    }]), r.directive("ngFile", ["$parse", function(e) {
        return {
            restrict: "A",
            link: function(r, n, i) {
                var a = e(i.ngFile),
                    t = a.assign;
                n.bind("change", function() {
                    r.$apply(function() {
                        t(r, n[0].files)
                    })
                })
            }
        }
    }]), r.directive("ngRightClick", ["$parse", function(e) {
        return function(r, n, i) {
            var a = e(i.ngRightClick);
            n.bind("contextmenu", function(e) {
                r.$apply(function() {
                    e.preventDefault(), a(r, {
                        $event: e
                    })
                })
            })
        }
    }])
}(angular),
function(e, r) {
    "use strict";
    e.module("FileManagerApp").controller("FileManagerCtrl", ["$scope", "$rootScope", "$window", "$translate", "fileManagerConfig", "item", "fileNavigator", "apiMiddleware", function(e, n, i, a, t, o, s, l) {
        var d = i.localStorage;
        e.config = t, e.reverse = !1, e.predicate = ["model.type", "model.name"], e.order = function(r) {
            e.reverse = e.predicate[1] === r ? !e.reverse : !1, e.predicate[1] = r
        }, e.query = "", e.fileNavigator = new s, e.apiMiddleware = new l, e.uploadFileList = [], e.viewTemplate = d.getItem("viewTemplate") || "main-icons.html", e.fileList = [], e.temps = [], e.$watch("temps", function() {
            e.singleSelection() ? e.temp = e.singleSelection() : (e.temp = new o({
                rights: 644
            }), e.temp.multiple = !0), e.temp.revert()
        }), e.fileNavigator.onRefresh = function() {
            e.temps = [], e.query = "", n.selectedModalPath = e.fileNavigator.currentPath
        }, e.setTemplate = function(r) {
            d.setItem("viewTemplate", r), e.viewTemplate = r
        }, e.changeLanguage = function(e) {
            return e 
        }, e.isSelected = function(r) {
            return -1 !== e.temps.indexOf(r)
        }, e.selectOrUnselect = function(r, n) {
            var i = e.temps.indexOf(r),
                a = n && 3 == n.which;
            if (n && n.target.hasAttribute("prevent")) return void(e.temps = []);
            if (!(!r || a && e.isSelected(r))) {
                if (n && n.shiftKey && !a) {
                    var t = e.fileList,
                        o = t.indexOf(r),
                        s = e.temps[0],
                        l = t.indexOf(s),
                        d = void 0;
                    if (s && t.indexOf(s) < o) {
                        for (e.temps = []; o >= l;) d = t[l], !e.isSelected(d) && e.temps.push(d), l++;
                        return
                    }
                    if (s && t.indexOf(s) > o) {
                        for (e.temps = []; l >= o;) d = t[l], !e.isSelected(d) && e.temps.push(d), l--;
                        return
                    }
                }
                return n && !a && (n.ctrlKey || n.metaKey) ? void(e.isSelected(r) ? e.temps.splice(i, 1) : e.temps.push(r)) : void(e.temps = [r])
            }
        }, e.singleSelection = function() {
            return 1 === e.temps.length && e.temps[0]
        }, e.totalSelecteds = function() {
            return {
                total: e.temps.length
            }
        }, e.selectionHas = function(r) {
            return e.temps.find(function(e) {
                return e && e.model.type === r
            })
        }, e.prepareNewFolder = function() {
            var r = new o(null, e.fileNavigator.currentPath);
            return e.temps = [r], r
        }, e.smartClick = function(r) {
            var n = e.config.allowedActions.pickFiles;
            if (r.isFolder()) return e.fileNavigator.folderClick(r);
            if ("function" == typeof e.config.pickCallback && n) {
                var i = e.config.pickCallback(r.model);
                if (i === !0) return
            }
            return r.isImage() ? e.config.previewImagesInModal ? e.openImagePreview(r) : e.apiMiddleware.download(r, !0) : r.isEditable() ? e.openEditItem(r) : void 0
        }, e.openImagePreview = function() {
            var r = e.singleSelection();
            e.apiMiddleware.apiHandler.inprocess = !0, e.modal("imagepreview", null, !0).find("#imagepreview-target").attr("src", e.apiMiddleware.getUrl(r)).unbind("load error").on("load error", function() {
                e.apiMiddleware.apiHandler.inprocess = !1, e.$apply()
            })
        }, e.openEditItem = function() {
            var r = e.singleSelection();
            e.apiMiddleware.getContent(r).then(function(e) {
                r.tempModel.content = r.model.content = e.result
            }), e.modal("edit")
        }, e.modal = function(n, i, a) {
            var t = r("#" + n);
            return t.modal(i ? "hide" : "show"), e.apiMiddleware.apiHandler.error = "", e.apiMiddleware.apiHandler.asyncSuccess = !1, a ? t : !0
        }, e.modalWithPathSelector = function(r) {
            return n.selectedModalPath = e.fileNavigator.currentPath, e.modal(r)
        }, e.isInThisPath = function(r) {
            var n = e.fileNavigator.currentPath.join("/");
            return -1 !== n.indexOf(r)
        }, e.edit = function() {
            e.apiMiddleware.edit(e.singleSelection()).then(function() {
                e.modal("edit", !0)
            })
        }, e.changePermissions = function() {
            e.apiMiddleware.changePermissions(e.temps, e.temp).then(function() {
                e.modal("changepermissions", !0)
            })
        }, e.download = function() {
            var r = e.singleSelection();
            if (!e.selectionHas("dir")) return r ? e.apiMiddleware.download(r) : e.apiMiddleware.downloadMultiple(e.temps)
        }, e.copy = function() {
            var r = e.singleSelection();
            if (r) {
                var i = r.tempModel.name.trim(),
                    t = e.fileNavigator.fileNameExists(i);
                if (t && c(r)) return e.apiMiddleware.apiHandler.error = a.instant("error_invalid_filename"), !1;
                if (!i) return e.apiMiddleware.apiHandler.error = a.instant("error_invalid_filename"), !1
            }
            e.apiMiddleware.copy(e.temps, n.selectedModalPath).then(function() {
                e.fileNavigator.refresh(), e.modal("copy", !0)
            })
        }, e.compress = function() {
            var r = e.temp.tempModel.name.trim(),
                i = e.fileNavigator.fileNameExists(r);
            return i && c(e.temp) ? (e.apiMiddleware.apiHandler.error = a.instant("error_invalid_filename"), !1) : r ? void e.apiMiddleware.compress(e.temps, r, n.selectedModalPath).then(function() {
                return e.fileNavigator.refresh(), e.config.compressAsync ? void(e.apiMiddleware.apiHandler.asyncSuccess = !0) : e.modal("compress", !0)
            }, function() {
                e.apiMiddleware.apiHandler.asyncSuccess = !1
            }) : (e.apiMiddleware.apiHandler.error = a.instant("error_invalid_filename"), !1)
        }, e.extract = function() {
            var r = e.temp,
                i = e.temp.tempModel.name.trim(),
                t = e.fileNavigator.fileNameExists(i);
            return t && c(e.temp) ? (e.apiMiddleware.apiHandler.error = a.instant("error_invalid_filename"), !1) : i ? void e.apiMiddleware.extract(r, i, n.selectedModalPath).then(function() {
                return e.fileNavigator.refresh(), e.config.extractAsync ? void(e.apiMiddleware.apiHandler.asyncSuccess = !0) : e.modal("extract", !0)
            }, function() {
                e.apiMiddleware.apiHandler.asyncSuccess = !1
            }) : (e.apiMiddleware.apiHandler.error = a.instant("error_invalid_filename"), !1)
        }, e.remove = function() {
            e.apiMiddleware.remove(e.temps).then(function() {
                e.fileNavigator.refresh(), e.modal("remove", !0)
            })
        }, e.move = function() {
            var r = e.singleSelection() || e.temps[0];
            return r && c(r) ? (e.apiMiddleware.apiHandler.error = a.instant("error_cannot_move_same_path"), !1) : void e.apiMiddleware.move(e.temps, n.selectedModalPath).then(function() {
                e.fileNavigator.refresh(), e.modal("move", !0)
            })
        }, e.rename = function() {
            var r = e.singleSelection(),
                n = r.tempModel.name,
                i = r.tempModel.path.join("") === r.model.path.join("");
            return !n || i && e.fileNavigator.fileNameExists(n) ? (e.apiMiddleware.apiHandler.error = a.instant("error_invalid_filename"), !1) : void e.apiMiddleware.rename(r).then(function() {
                e.fileNavigator.refresh(), e.modal("rename", !0)
            })
        }, e.createFolder = function() {
            var r = e.singleSelection(),
                n = r.tempModel.name;
            return !n || e.fileNavigator.fileNameExists(n) ? e.apiMiddleware.apiHandler.error = a.instant("error_invalid_filename") : void e.apiMiddleware.createFolder(r).then(function() {
                e.fileNavigator.refresh(), e.modal("newfolder", !0)
            })
        }, e.addForUpload = function(r) {
            e.uploadFileList = e.uploadFileList.concat(r), e.modal("uploadfile")
        }, e.removeFromUpload = function(r) {
            e.uploadFileList.splice(r, 1)
        }, e.uploadFiles = function() {
            e.apiMiddleware.upload(e.uploadFileList, e.fileNavigator.currentPath).then(function() {
                e.fileNavigator.refresh(), e.uploadFileList = [], e.modal("uploadfile", !0)
            }, function(r) {
                var n = r.result && r.result.error || a.instant("error_uploading_files");
                e.apiMiddleware.apiHandler.error = n
            })
        };
        var c = function(e) {
                var r = n.selectedModalPath.join(""),
                    i = e && e.model.path.join("");
                return i === r
            },
            p = function(e) {
                var r = i.location.search.substr(1).split("&").filter(function(r) {
                    return e === r.split("=")[0]
                });
                return r[0] && r[0].split("=")[1] || void 0
            };
        e.changeLanguage(p("lang")), e.isWindows = "Windows" === p("server"), e.fileNavigator.refresh()
    }])
}(angular, jQuery),
function(e) {
    "use strict";
    e.module("FileManagerApp").controller("ModalFileManagerCtrl", ["$scope", "$rootScope", "fileNavigator", function(e, r, n) {
        e.reverse = !1, e.predicate = ["model.type", "model.name"], e.fileNavigator = new n, r.selectedModalPath = [], e.order = function(r) {
            e.reverse = e.predicate[1] === r ? !e.reverse : !1, e.predicate[1] = r
        }, e.select = function(n) {
            r.selectedModalPath = n.model.fullPath().split("/"), e.modal("selector", !0)
        }, e.selectCurrent = function() {
            r.selectedModalPath = e.fileNavigator.currentPath, e.modal("selector", !0)
        }, e.selectedFilesAreChildOfPath = function(r) {
            var n = r.model.fullPath();
            return e.temps.find(function(e) {
                var r = e.model.fullPath();
                return n == r ? !0 : void 0
            })
        }, r.openNavigator = function(r) {
            e.fileNavigator.currentPath = r, e.fileNavigator.refresh(), e.modal("selector")
        }, r.getSelectedPath = function() {
            var n = r.selectedModalPath.filter(Boolean),
                i = "/" + n.join("/");
            return e.singleSelection() && !e.singleSelection().isFolder() && (i += "/" + e.singleSelection().tempModel.name), i.replace(/\/\//, "/")
        }
    }])
}(angular),
function(e) {
    "use strict";
    e.module("FileManagerApp").service("chmod", function() {
        var e = function(e) {
            if (this.owner = this.getRwxObj(), this.group = this.getRwxObj(), this.others = this.getRwxObj(), e) {
                var r = isNaN(e) ? this.convertfromCode(e) : this.convertfromOctal(e);
                if (!r) throw new Error("Invalid chmod input data (%s)".replace("%s", e));
                this.owner = r.owner, this.group = r.group, this.others = r.others
            }
        };
        return e.prototype.toOctal = function(e, r) {
            var n = [];
            return ["owner", "group", "others"].forEach(function(e, r) {
                n[r] = this[e].read && this.octalValues.read || 0, n[r] += this[e].write && this.octalValues.write || 0, n[r] += this[e].exec && this.octalValues.exec || 0
            }.bind(this)), (e || "") + n.join("") + (r || "")
        }, e.prototype.toCode = function(e, r) {
            var n = [];
            return ["owner", "group", "others"].forEach(function(e, r) {
                n[r] = this[e].read && this.codeValues.read || "-", n[r] += this[e].write && this.codeValues.write || "-", n[r] += this[e].exec && this.codeValues.exec || "-"
            }.bind(this)), (e || "") + n.join("") + (r || "")
        }, e.prototype.getRwxObj = function() {
            return {
                read: !1,
                write: !1,
                exec: !1
            }
        }, e.prototype.octalValues = {
            read: 4,
            write: 2,
            exec: 1
        }, e.prototype.codeValues = {
            read: "r",
            write: "w",
            exec: "x"
        }, e.prototype.convertfromCode = function(e) {
            if (e = ("" + e).replace(/\s/g, ""), e = 10 === e.length ? e.substr(1) : e, /^[-rwxts]{9}$/.test(e)) {
                var r = [],
                    n = e.match(/.{1,3}/g);
                for (var i in n) {
                    var a = this.getRwxObj();
                    a.read = /r/.test(n[i]), a.write = /w/.test(n[i]), a.exec = /x|t/.test(n[i]), r.push(a)
                }
                return {
                    owner: r[0],
                    group: r[1],
                    others: r[2]
                }
            }
        }, e.prototype.convertfromOctal = function(e) {
            if (e = ("" + e).replace(/\s/g, ""), e = 4 === e.length ? e.substr(1) : e, /^[0-7]{3}$/.test(e)) {
                var r = [],
                    n = e.match(/.{1}/g);
                for (var i in n) {
                    var a = this.getRwxObj();
                    a.read = /[4567]/.test(n[i]), a.write = /[2367]/.test(n[i]), a.exec = /[1357]/.test(n[i]), r.push(a)
                }
                return {
                    owner: r[0],
                    group: r[1],
                    others: r[2]
                }
            }
        }, e
    })
}(angular),
function(e) {
    "use strict";
    e.module("FileManagerApp").factory("item", ["fileManagerConfig", "chmod", function(r, n) {
        var i = function(r, i) {
            function a(e) {
                var r = (e || "").toString().split(/[- :]/);
                return new Date(r[0], r[1] - 1, r[2], r[3], r[4], r[5])
            }
            var t = {
                name: r && r.name || "",
                path: i || [],
                type: r && r.type || "file",
                size: r && parseInt(r.size || 0),
                date: a(r && r.date),
                perms: new n(r && r.rights),
                content: r && r.content || "",
                recursive: !1,
                fullPath: function() {
                    var e = this.path.filter(Boolean);
                    return ("/" + e.join("/") + "/" + this.name).replace(/\/\//, "/")
                }
            };
            this.error = "", this.processing = !1, this.model = e.copy(t), this.tempModel = e.copy(t)
        };
        return i.prototype.update = function() {
            e.extend(this.model, e.copy(this.tempModel))
        }, i.prototype.revert = function() {
            e.extend(this.tempModel, e.copy(this.model)), this.error = ""
        }, i.prototype.isFolder = function() {
            return "dir" === this.model.type
        }, i.prototype.isEditable = function() {
            return !this.isFolder() && r.isEditableFilePattern.test(this.model.name)
        }, i.prototype.isImage = function() {
            return r.isImageFilePattern.test(this.model.name)
        }, i.prototype.isCompressible = function() {
            return this.isFolder()
        }, i.prototype.isExtractable = function() {
            return !this.isFolder() && r.isExtractableFilePattern.test(this.model.name)
        }, i.prototype.isSelectable = function() {
            return this.isFolder() && r.allowedActions.pickFolders || !this.isFolder() && r.allowedActions.pickFiles
        }, i
    }])
}(angular),
function(e) {
    "use strict";
    var r = e.module("FileManagerApp");
    r.filter("strLimit", ["$filter", function(e) {
        return function(r, n, i) {
            return r.length <= n ? r : e("limitTo")(r, n) + (i || "...")
        }
    }]), r.filter("fileExtension", ["$filter", function(e) {
        return function(r) {
            return /\./.test(r) && e("strLimit")(r.split(".").pop(), 3, "..") || ""
        }
    }]), r.filter("formatDate", ["$filter", function() {
        return function(e) {
            return ''
        }
    }]), r.filter("humanReadableFileSize", ["$filter", "fileManagerConfig", function(e, r) {
        var n = [" kB", " MB", " GB", " TB", "PB", "EB", "ZB", "YB"],
            i = ["KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB"];
        return function(e) {
            var a = -1,
                t = e;
            do t /= 1024, a++; while (t > 1024);
            var o = r.useBinarySizePrefixes ? i[a] : n[a];
            return Math.max(t, .1).toFixed(1) + " " + o
        }
    }])
}(angular),
function(e, r) {
    "use strict";
    e.module("FileManagerApp").service("apiHandler", ["$http", "$q", "$window", "$translate", "Upload", function(e, n, i, a, t) {
        e.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
        var o = function() {
            this.inprocess = !1, this.asyncSuccess = !1, this.error = ""
        };
        return o.prototype.deferredHandler = function(e, r, n, i) {
            return e && "object" == typeof e || (this.error = "Error %s - Bridge response error, please check the API docs or this ajax response.".replace("%s", n)), 404 == n && (this.error = "Error 404 - Backend bridge is not working, please check the ajax response."), e.result && e.result.error && (this.error = e.result.error), !this.error && e.error && (this.error = e.error.message), !this.error && i && (this.error = i), this.error ? r.reject(e) : r.resolve(e)
        }, o.prototype.list = function(r, i, a) {
            var t = this,
                o = a || t.deferredHandler,
                s = n.defer(),
                l = {
                    action: "list",
                    path: i
                };
            return t.inprocess = !0, t.error = "", e.post(r, l).success(function(e, r) {
                o(e, s, r)
            }).error(function(e, r) {
                o(e, s, r, "Unknown error listing, check the response")
            })["finally"](function() {
                t.inprocess = !1
            }), s.promise
        }, o.prototype.copy = function(r, i, t, o) {
            var s = this,
                l = n.defer(),
                d = {
                    action: "copy",
                    items: i,
                    newPath: t
                };
            return o && 1 === i.length && (d.singleFilename = o), s.inprocess = !0, s.error = "", e.post(r, d).success(function(e, r) {
                s.deferredHandler(e, l, r)
            }).error(function(e, r) {
                s.deferredHandler(e, l, r, a.instant("error_copying"))
            })["finally"](function() {
                s.inprocess = !1
            }), l.promise
        }, o.prototype.move = function(r, i, t) {
            var o = this,
                s = n.defer(),
                l = {
                    action: "move",
                    items: i,
                    newPath: t
                };
            return o.inprocess = !0, o.error = "", e.post(r, l).success(function(e, r) {
                o.deferredHandler(e, s, r)
            }).error(function(e, r) {
                o.deferredHandler(e, s, r, a.instant("error_moving"))
            })["finally"](function() {
                o.inprocess = !1
            }), s.promise
        }, o.prototype.remove = function(r, i) {
            var t = this,
                o = n.defer(),
                s = {
                    action: "remove",
                    items: i
                };
            return t.inprocess = !0, t.error = "", e.post(r, s).success(function(e, r) {
                t.deferredHandler(e, o, r)
            }).error(function(e, r) {
                t.deferredHandler(e, o, r, a.instant("error_deleting"))
            })["finally"](function() {
                t.inprocess = !1
            }), o.promise
        }, o.prototype.upload = function(e, r, i) {
            var a = this,
                o = n.defer();
            a.inprocess = !0, a.progress = 0, a.error = "";
            for (var s = {
                    destination: r
                }, l = 0; l < i.length; l++) s["file-" + l] = i[l];
            return i && i.length && t.upload({
                url: e,
                data: s
            }).then(function(e) {
                a.deferredHandler(e.data, o, e.status)
            }, function(e) {
                a.deferredHandler(e.data, o, e.status, "Unknown error uploading files")
            }, function(e) {
                a.progress = Math.min(100, parseInt(100 * e.loaded / e.total)) - 1
            })["finally"](function() {
                a.inprocess = !1, a.progress = 0
            }), o.promise
        }, o.prototype.getContent = function(r, i) {
            var t = this,
                o = n.defer(),
                s = {
                    action: "getContent",
                    item: i
                };
            return t.inprocess = !0, t.error = "", e.post(r, s).success(function(e, r) {
                t.deferredHandler(e, o, r)
            }).error(function(e, r) {
                t.deferredHandler(e, o, r, a.instant("error_getting_content"))
            })["finally"](function() {
                t.inprocess = !1
            }), o.promise
        }, o.prototype.edit = function(r, i, t) {
            var o = this,
                s = n.defer(),
                l = {
                    action: "edit",
                    item: i,
                    content: t
                };
            return o.inprocess = !0, o.error = "", e.post(r, l).success(function(e, r) {
                o.deferredHandler(e, s, r)
            }).error(function(e, r) {
                o.deferredHandler(e, s, r, a.instant("error_modifying"))
            })["finally"](function() {
                o.inprocess = !1
            }), s.promise
        }, o.prototype.rename = function(r, i, t) {
            var o = this,
                s = n.defer(),
                l = {
                    action: "rename",
                    item: i,
                    newItemPath: t
                };
            return o.inprocess = !0, o.error = "", e.post(r, l).success(function(e, r) {
                o.deferredHandler(e, s, r)
            }).error(function(e, r) {
                o.deferredHandler(e, s, r, a.instant("error_renaming"))
            })["finally"](function() {
                o.inprocess = !1
            }), s.promise
        }, o.prototype.getUrl = function(e, n) {
            var i = {
                action: "download",
                path: n
            };
            return n && [e, r.param(i)].join("?")
        }, o.prototype.download = function(r, t, o, s, l) {
            var d = this,
                c = this.getUrl(r, t);
            if (!s || l || !i.saveAs) return !i.saveAs && i.console.log("Your browser dont support ajax download, downloading by default"), !!i.open(c, "_blank", "");
            var p = n.defer();
            return d.inprocess = !0, e.get(c).success(function(e) {
                var r = new i.Blob([e]);
                p.resolve(e), i.saveAs(r, o)
            }).error(function(e, r) {
                d.deferredHandler(e, p, r, a.instant("error_downloading"))
            })["finally"](function() {
                d.inprocess = !1
            }), p.promise
        }, o.prototype.downloadMultiple = function(t, o, s, l, d) {
            var c = this,
                p = n.defer(),
                m = {
                    action: "downloadMultiple",
                    items: o,
                    toFilename: s
                },
                u = [t, r.param(m)].join("?");
            return l && !d && i.saveAs ? (c.inprocess = !0, e.get(t).success(function(e) {
                var r = new i.Blob([e]);
                p.resolve(e), i.saveAs(r, s)
            }).error(function(e, r) {
                c.deferredHandler(e, p, r, a.instant("error_downloading"))
            })["finally"](function() {
                c.inprocess = !1
            }), p.promise) : (!i.saveAs && i.console.log("Your browser dont support ajax download, downloading by default"), !!i.open(u, "_blank", ""))
        }, o.prototype.compress = function(r, i, t, o) {
            var s = this,
                l = n.defer(),
                d = {
                    action: "compress",
                    items: i,
                    destination: o,
                    compressedFilename: t
                };
            return s.inprocess = !0, s.error = "", e.post(r, d).success(function(e, r) {
                s.deferredHandler(e, l, r)
            }).error(function(e, r) {
                s.deferredHandler(e, l, r, a.instant("error_compressing"))
            })["finally"](function() {
                s.inprocess = !1
            }), l.promise
        }, o.prototype.extract = function(r, i, t, o) {
            var s = this,
                l = n.defer(),
                d = {
                    action: "extract",
                    item: i,
                    destination: o,
                    folderName: t
                };
            return s.inprocess = !0, s.error = "", e.post(r, d).success(function(e, r) {
                s.deferredHandler(e, l, r)
            }).error(function(e, r) {
                s.deferredHandler(e, l, r, a.instant("error_extracting"))
            })["finally"](function() {
                s.inprocess = !1
            }), l.promise
        }, o.prototype.changePermissions = function(r, i, t, o, s) {
            var l = this,
                d = n.defer(),
                c = {
                    action: "changePermissions",
                    items: i,
                    perms: t,
                    permsCode: o,
                    recursive: !!s
                };
            return l.inprocess = !0, l.error = "", e.post(r, c).success(function(e, r) {
                l.deferredHandler(e, d, r)
            }).error(function(e, r) {
                l.deferredHandler(e, d, r, a.instant("error_changing_perms"))
            })["finally"](function() {
                l.inprocess = !1
            }), d.promise
        }, o.prototype.createFolder = function(r, i) {
            var t = this,
                o = n.defer(),
                s = {
                    action: "createFolder",
                    newPath: i
                };
            return t.inprocess = !0, t.error = "", e.post(r, s).success(function(e, r) {
                t.deferredHandler(e, o, r)
            }).error(function(e, r) {
                t.deferredHandler(e, o, r, a.instant("error_creating_folder"))
            })["finally"](function() {
                t.inprocess = !1
            }), o.promise
        }, o
    }])
}(angular, jQuery),
function(e) {
    "use strict";
    e.module("FileManagerApp").service("apiMiddleware", ["$window", "fileManagerConfig", "apiHandler", function(e, r, n) {
        var i = function() {
            this.apiHandler = new n
        };
        return i.prototype.getPath = function(e) {
            return "/" + e.join("/")
        }, i.prototype.getFileList = function(e) {
            return (e || []).map(function(e) {
                return e && e.model.fullPath()
            })
        }, i.prototype.getFilePath = function(e) {
            return e && e.model.fullPath()
        }, i.prototype.list = function(e, n) {
            return this.apiHandler.list(r.listUrl, this.getPath(e), n)
        }, i.prototype.copy = function(e, n) {
            var i = this.getFileList(e),
                a = 1 === i.length ? e[0].tempModel.name : void 0;
            return this.apiHandler.copy(r.copyUrl, i, this.getPath(n), a)
        }, i.prototype.move = function(e, n) {
            var i = this.getFileList(e);
            return this.apiHandler.move(r.moveUrl, i, this.getPath(n))
        }, i.prototype.remove = function(e) {
            var n = this.getFileList(e);
            return this.apiHandler.remove(r.removeUrl, n)
        }, i.prototype.upload = function(n, i) {
            if (!e.FormData) throw new Error("Unsupported browser version");
            var a = this.getPath(i);
            return this.apiHandler.upload(r.uploadUrl, a, n)
        }, i.prototype.getContent = function(e) {
            var n = this.getFilePath(e);
            return this.apiHandler.getContent(r.getContentUrl, n)
        }, i.prototype.edit = function(e) {
            var n = this.getFilePath(e);
            return this.apiHandler.edit(r.editUrl, n, e.tempModel.content)
        }, i.prototype.rename = function(e) {
            var n = this.getFilePath(e),
                i = e.tempModel.fullPath();
            return this.apiHandler.rename(r.renameUrl, n, i)
        }, i.prototype.getUrl = function(e) {
            var n = this.getFilePath(e);
            return this.apiHandler.getUrl(r.downloadFileUrl, n)
        }, i.prototype.download = function(e, n) {
            var i = this.getFilePath(e),
                a = e.model.name;
            return e.isFolder() ? void 0 : this.apiHandler.download(r.downloadFileUrl, i, a, r.downloadFilesByAjax, n)
        }, i.prototype.downloadMultiple = function(e, n) {
            var i = this.getFileList(e),
                a = (new Date).getTime().toString().substr(8, 13),
                t = a + "-" + r.multipleDownloadFileName;
            return this.apiHandler.downloadMultiple(r.downloadMultipleUrl, i, t, r.downloadFilesByAjax, n)
        }, i.prototype.compress = function(e, n, i) {
            var a = this.getFileList(e);
            return this.apiHandler.compress(r.compressUrl, a, n, this.getPath(i))
        }, i.prototype.extract = function(e, n, i) {
            var a = this.getFilePath(e);
            return this.apiHandler.extract(r.extractUrl, a, n, this.getPath(i))
        }, i.prototype.changePermissions = function(e, n) {
            var i = this.getFileList(e),
                a = n.tempModel.perms.toCode(),
                t = n.tempModel.perms.toOctal(),
                o = !!n.tempModel.recursive;
            return this.apiHandler.changePermissions(r.permissionsUrl, i, a, t, o)
        }, i.prototype.createFolder = function(e) {
            var n = e.tempModel.fullPath();
            return this.apiHandler.createFolder(r.createFolderUrl, n)
        }, i
    }])
}(angular),
function(e) {
    "use strict";
    e.module("FileManagerApp").service("fileNavigator", ["apiMiddleware", "fileManagerConfig", "item", function(e, r, n) {
        var i = function() {
            this.apiMiddleware = new e, this.requesting = !1, this.fileList = [], this.currentPath = [], this.history = [], this.error = "", this.onRefresh = function() {}
        };
        return i.prototype.deferredHandler = function(e, r, n, i) {
            return e && "object" == typeof e || (this.error = "Error %s - Bridge response error, please check the API docs or this ajax response.".replace("%s", n)), 404 == n && (this.error = "Error 404 - Backend bridge is not working, please check the ajax response."), !this.error && e.result && e.result.error && (this.error = e.result.error), !this.error && e.error && (this.error = e.error.message), !this.error && i && (this.error = i), this.error ? r.reject(e) : r.resolve(e)
        }, i.prototype.list = function() {
            return this.apiMiddleware.list(this.currentPath, this.deferredHandler.bind(this))
        }, i.prototype.refresh = function() {
            var e = this,
                r = e.currentPath.join("/");
            return e.requesting = !0, e.fileList = [], e.list().then(function(i) {
                e.fileList = (i.result || []).map(function(r) {
                    return new n(r, e.currentPath)
                }), e.buildTree(r), e.onRefresh()
            })["finally"](function() {
                e.requesting = !1
            })
        }, i.prototype.buildTree = function(e) {
            function r(e, n, i) {
                var a = i ? i + "/" + n.model.name : n.model.name;
                if (e.name.trim() && 0 !== i.trim().indexOf(e.name) && (e.nodes = []), e.name !== i) e.nodes.forEach(function(e) {
                    r(e, n, i)
                });
                else {
                    for (var t in e.nodes)
                        if (e.nodes[t].name === a) return;
                    e.nodes.push({
                        item: n,
                        name: a,
                        nodes: []
                    })
                }
                e.nodes = e.nodes.sort(function(e, r) {
                    return e.name.toLowerCase() < r.name.toLowerCase() ? -1 : e.name.toLowerCase() === r.name.toLowerCase() ? 0 : 1
                })
            }

            function i(e, r) {
                r.push(e);
                for (var n in e.nodes) i(e.nodes[n], r)
            }

            function a(e, r) {
                return e.filter(function(e) {
                    return e.name === r
                })[0]
            }
            var t = [],
                o = {};
            !this.history.length && this.history.push({
                name: "",
                nodes: []
            }), i(this.history[0], t), o = a(t, e), o && (o.nodes = []);
            for (var s in this.fileList) {
                var l = this.fileList[s];
                l instanceof n && l.isFolder() && r(this.history[0], l, e)
            }
        }, i.prototype.folderClick = function(e) {
            this.currentPath = [], e && e.isFolder() && (this.currentPath = e.model.fullPath().split("/").splice(1)), this.refresh()
        }, i.prototype.upDir = function() {
            this.currentPath[0] && (this.currentPath = this.currentPath.slice(0, -1), this.refresh())
        }, i.prototype.goTo = function(e) {
            this.currentPath = this.currentPath.slice(0, e + 1), this.refresh()
        }, i.prototype.fileNameExists = function(e) {
            return this.fileList.find(function(r) {
                return e.trim && r.model.name.trim() === e.trim()
            })
        }, i.prototype.listHasFolders = function() {
            return this.fileList.find(function(e) {
                return "dir" === e.model.type
            })
        }, i
    }])
}(angular),
function(e) {
    "use strict";
    e.module("FileManagerApp").provider("fileManagerConfig", function() {
        var r = {
            appName: "angular-filemanager v1.5",
            defaultLang: "en",
            listUrl: "bridges/php/handler.php",
            uploadUrl: "bridges/php/handler.php",
            renameUrl: "bridges/php/handler.php",
            copyUrl: "bridges/php/handler.php",
            moveUrl: "bridges/php/handler.php",
            removeUrl: "bridges/php/handler.php",
            editUrl: "bridges/php/handler.php",
            getContentUrl: "bridges/php/handler.php",
            createFolderUrl: "bridges/php/handler.php",
            downloadFileUrl: "bridges/php/handler.php",
            downloadMultipleUrl: "bridges/php/handler.php",
            compressUrl: "bridges/php/handler.php",
            extractUrl: "bridges/php/handler.php",
            permissionsUrl: "bridges/php/handler.php",
            searchForm: !0,
            sidebar: !0,
            breadcrumb: !0,
            allowedActions: {
                upload: !0,
                rename: !0,
                move: !0,
                copy: !0,
                edit: !0,
                changePermissions: !0,
                compress: !0,
                compressChooseName: !0,
                extract: !0,
                download: !0,
                downloadMultiple: !0,
                preview: !0,
                remove: !0,
                createFolder: !0,
                pickFiles: !1,
                pickFolders: !1
            },
            multipleDownloadFileName: "angular-filemanager.zip",
            showExtensionIcons: !0,
            showSizeForDirectories: !1,
            useBinarySizePrefixes: !1,
            downloadFilesByAjax: !0,
            previewImagesInModal: !0,
            enablePermissionsRecursive: !0,
            compressAsync: !1,
            extractAsync: !1,
            pickCallback: null,
            isEditableFilePattern: /\.(txt|diff?|patch|svg|asc|cnf|cfg|conf|html?|.html|cfm|cgi|aspx?|ini|pl|py|md|css|cs|js|jsp|log|htaccess|htpasswd|gitignore|gitattributes|env|json|atom|eml|rss|markdown|sql|xml|xslt?|sh|rb|as|bat|cmd|cob|for|ftn|frm|frx|inc|lisp|scm|coffee|php[3-6]?|java|c|cbl|go|h|scala|vb|tmpl|lock|go|yml|yaml|tsv|lst)$/i,
            isImageFilePattern: /\.(jpe?g|gif|bmp|png|svg|tiff?)$/i,
            isExtractableFilePattern: /\.(gz|tar|rar|g?zip)$/i,
            tplPath: "src/templates"
        };
        return {
            $get: function() {
                return r
            },
            set: function(n) {
                e.extend(r, n)
            }
        }
    })
}(angular),
function(e) {
    "use strict";
    e.module("FileManagerApp").config(["$translateProvider", function(e) {
        e.useSanitizeValueStrategy(null), e.translations("en", {
            filemanager: "File Manager",
            language: "Language",
            english: "English",
            spanish: "Spanish",
            portuguese: "Portuguese",
            french: "French",
            german: "German",
            hebrew: "Hebrew",
            slovak: "Slovak",
            chinese: "Chinese",
            russian: "Russian",
            ukrainian: "Ukrainian",
            turkish: "Turkish",
            persian: "Persian",
            confirm: "Confirm",
            cancel: "Cancel",
            close: "Close",
            upload_files: "Upload files",
            files_will_uploaded_to: "Files will be uploaded to",
            select_files: "Select files",
            uploading: "Uploading",
            permissions: "Permissions",
            select_destination_folder: "Select the destination folder",
            source: "Source",
            destination: "Destination",
            copy_file: "Copy file",
            sure_to_delete: "Are you sure to delete",
            change_name_move: "Change name / move",
            enter_new_name_for: "Enter new name for",
            extract_item: "Extract item",
            extraction_started: "Extraction started in a background process",
            compression_started: "Compression started in a background process",
            enter_folder_name_for_extraction: "Enter the folder name for the extraction of",
            enter_file_name_for_compression: "Enter the file name for the compression of",
            toggle_fullscreen: "Toggle fullscreen",
            edit_file: "Edit file",
            file_content: "File content",
            loading: "Loading",
            search: "Search",
            create_folder: "Create folder",
            create: "Create",
            folder_name: "Folder name",
            upload: "Upload",
            change_permissions: "Change permissions",
            change: "Change",
            details: "Details",
            icons: "Icons",
            list: "List",
            name: "Name",
            size: "Size",
            actions: "Actions",
            date: "Date",
            no_files_in_folder: "No files in this folder",
            no_folders_in_folder: "This folder not contains children folders",
            select_this: "Select this",
            go_back: "Go back",
            wait: "Wait",
            move: "Move",
            download: "Download",
            view_item: "View item",
            remove: "Delete",
            edit: "Edit",
            copy: "Copy",
            rename: "Rename",
            extract: "Extract",
            compress: "Compress",
            error_invalid_filename: "Invalid filename or already exists, specify another name",
            error_modifying: "An error occurred modifying the file",
            error_deleting: "An error occurred deleting the file or folder",
            error_renaming: "An error occurred renaming the file",
            error_copying: "An error occurred copying the file",
            error_compressing: "An error occurred compressing the file or folder",
            error_extracting: "An error occurred extracting the file",
            error_creating_folder: "An error occurred creating the folder",
            error_getting_content: "An error occurred getting the content of the file",
            error_changing_perms: "An error occurred changing the permissions of the file",
            error_uploading_files: "An error occurred uploading files",
            sure_to_start_compression_with: "Are you sure to compress",
            owner: "Owner",
            group: "Group",
            others: "Others",
            read: "Read",
            write: "Write",
            exec: "Exec",
            original: "Original",
            changes: "Changes",
            recursive: "Recursive",
            preview: "Item preview",
            open: "Open",
            these_elements: "these {{total}} elements",
            new_folder: "New folder",
            download_as_zip: "Download as ZIP"
        }), e.translations("he", {
            filemanager: "מנהל קבצים",
            language: "שפה",
            english: "אנגלית",
            spanish: "ספרדית",
            portuguese: "פורטוגזית",
            french: "צרפתית",
            german: "גרמנית",
            hebrew: "עברי",
            slovak: "סלובקי",
            chinese: "סִינִית",
            russian: "רוּסִי",
            ukrainian: "אוקראיני",
            turkish: "טורקי",
            persian: "פַּרסִית",
            confirm: "אשר",
            cancel: "בטל",
            close: "סגור",
            upload_files: "העלה קבצים",
            files_will_uploaded_to: "הקבצים יעלו ל",
            select_files: "בחר קבצים",
            uploading: "מעלה",
            permissions: "הרשאות",
            select_destination_folder: "בחר תיקיית יעד",
            source: "מקור",
            destination: "יעד",
            copy_file: "העתק קובץ",
            sure_to_delete: "האם אתה בטוח שברצונך למחוק",
            change_name_move: "שנה שם / הזז",
            enter_new_name_for: "הקלד שם חדש עבור",
            extract_item: "חלץ פריט",
            extraction_started: "תהליך החילוץ מתבצע ברקע",
            compression_started: "תהליך הכיווץ מתבצע ברקע",
            enter_folder_name_for_extraction: "הקלד שם תיקייה לחילוץ עבור",
            enter_file_name_for_compression: "הזן את שם הקובץ עבור הדחיסה של",
            toggle_fullscreen: "הפעל/בטל מסך מלא",
            edit_file: "ערוך קובץ",
            file_content: "תוכן הקובץ",
            loading: "טוען",
            search: "חפש",
            create_folder: "צור תיקייה",
            create: "צור",
            folder_name: "שם תיקייה",
            upload: "העלה",
            change_permissions: "שנה הרשאות",
            change: "שנה",
            details: "פרטים",
            icons: "סמלים",
            list: "רשימה",
            name: "שם",
            size: "גודל",
            actions: "פעולות",
            date: "תאריך",
            no_files_in_folder: "אין קבצים בתיקייה זו",
            no_folders_in_folder: "התיקייה הזו אינה כוללת תתי תיקיות",
            select_this: "בחר את זה",
            go_back: "חזור אחורה",
            wait: "חכה",
            move: "הזז",
            download: "הורד",
            view_item: "הצג פריט",
            remove: "מחק",
            edit: "ערוך",
            copy: "העתק",
            rename: "שנה שם",
            extract: "חלץ",
            compress: "כווץ",
            error_invalid_filename: "שם קובץ אינו תקין או קיים, ציין שם קובץ אחר",
            error_modifying: "התרחשה שגיאה בעת שינוי הקובץ",
            error_deleting: "התרחשה שגיאה בעת מחיקת הקובץ או התיקייה",
            error_renaming: "התרחשה שגיאה בעת שינוי שם הקובץ",
            error_copying: "התרחשה שגיאה בעת העתקת הקובץ",
            error_compressing: "התרחשה שגיאה בעת כיווץ הקובץ או התיקייה",
            error_extracting: "התרחשה שגיאה בעת חילוץ הקובץ או התיקייה",
            error_creating_folder: "התרחשה שגיאה בעת יצירת התיקייה",
            error_getting_content: "התרחשה שגיאה בעת בקשת תוכן הקובץ",
            error_changing_perms: "התרחשה שגיאה בעת שינוי הרשאות הקובץ",
            error_uploading_files: "התרחשה שגיאה בעת העלאת הקבצים",
            sure_to_start_compression_with: "האם אתה בטוח שברצונך לכווץ",
            owner: "בעלים",
            group: "קבוצה",
            others: "אחרים",
            read: "קריאה",
            write: "כתיבה",
            exec: "הרצה",
            original: "מקורי",
            changes: "שינויים",
            recursive: "רקורסיה",
            preview: "הצגת פריט",
            open: "פתח",
            new_folder: "תיקיה חדשה",
            download_as_zip: "להוריד כמו"
        }), e.translations("pt", {
            filemanager: "Gerenciador de arquivos",
            language: "Língua",
            english: "Inglês",
            spanish: "Espanhol",
            portuguese: "Portugues",
            french: "Francês",
            german: "Alemão",
            hebrew: "Hebraico",
            slovak: "Eslovaco",
            chinese: "Chinês",
            russian: "Russo",
            ukrainian: "Ucraniano",
            turkish: "Turco",
            persian: "Persa",
            confirm: "Confirmar",
            cancel: "Cancelar",
            close: "Fechar",
            upload_files: "Carregar arquivos",
            files_will_uploaded_to: "Os arquivos serão enviados para",
            select_files: "Selecione os arquivos",
            uploading: "Carregar",
            permissions: "Autorizações",
            select_destination_folder: "Selecione a pasta de destino",
            source: "Origem",
            destination: "Destino",
            copy_file: "Copiar arquivo",
            sure_to_delete: "Tem certeza de que deseja apagar",
            change_name_move: "Renomear / mudança",
            enter_new_name_for: "Digite o novo nome para",
            extract_item: "Extrair arquivo",
            extraction_started: "A extração começou em um processo em segundo plano",
            compression_started: "A compressão começou em um processo em segundo plano",
            enter_folder_name_for_extraction: "Digite o nome da pasta para a extração de",
            enter_file_name_for_compression: "Digite o nome do arquivo para a compressão de",
            toggle_fullscreen: "Ativar/desativar tela cheia",
            edit_file: "Editar arquivo",
            file_content: "Conteúdo do arquivo",
            loading: "Carregando",
            search: "Localizar",
            create_folder: "Criar Pasta",
            create: "Criar",
            folder_name: "Nome da pasta",
            upload: "Fazer",
            change_permissions: "Alterar permissões",
            change: "Alterar",
            details: "Detalhes",
            icons: "Icones",
            list: "Lista",
            name: "Nome",
            size: "Tamanho",
            actions: "Ações",
            date: "Data",
            no_files_in_folder: "Não há arquivos nesta pasta",
            no_folders_in_folder: "Esta pasta não contém subpastas",
            select_this: "Selecione esta",
            go_back: "Voltar",
            wait: "Espere",
            move: "Mover",
            download: "Baixar",
            view_item: "Veja o arquivo",
            remove: "Excluir",
            edit: "Editar",
            copy: "Copiar",
            rename: "Renomear",
            extract: "Extrair",
            compress: "Comprimir",
            error_invalid_filename: "Nome do arquivo inválido ou nome de arquivo já existe, especifique outro nome",
            error_modifying: "Ocorreu um erro ao modificar o arquivo",
            error_deleting: "Ocorreu um erro ao excluir o arquivo ou pasta",
            error_renaming: "Ocorreu um erro ao mudar o nome do arquivo",
            error_copying: "Ocorreu um erro ao copiar o arquivo",
            error_compressing: "Ocorreu um erro ao comprimir o arquivo ou pasta",
            error_extracting: "Ocorreu um erro ao extrair o arquivo",
            error_creating_folder: "Ocorreu um erro ao criar a pasta",
            error_getting_content: "Ocorreu um erro ao obter o conteúdo do arquivo",
            error_changing_perms: "Ocorreu um erro ao alterar as permissões do arquivo",
            error_uploading_files: "Ocorreu um erro upload de arquivos",
            sure_to_start_compression_with: "Tem certeza que deseja comprimir",
            owner: "Proprietário",
            group: "Grupo",
            others: "Outros",
            read: "Leitura",
            write: "Escrita ",
            exec: "Execução",
            original: "Original",
            changes: "Mudanças",
            recursive: "Recursiva",
            preview: "Visualização",
            open: "Abrir",
            these_elements: "estes {{total}} elements",
            new_folder: "Nova pasta",
            download_as_zip: "Download como ZIP"
        }), e.translations("es", {
            filemanager: "Administrador de archivos",
            language: "Idioma",
            english: "Ingles",
            spanish: "Español",
            portuguese: "Portugues",
            french: "Francés",
            german: "Alemán",
            hebrew: "Hebreo",
            slovak: "Eslovaco",
            chinese: "Chino",
            russian: "Ruso",
            ukrainian: "Ucraniano",
            turkish: "Turco",
            persian: "Persa",
            confirm: "Confirmar",
            cancel: "Cancelar",
            close: "Cerrar",
            upload_files: "Subir archivos",
            files_will_uploaded_to: "Los archivos seran subidos a",
            select_files: "Seleccione los archivos",
            uploading: "Subiendo",
            permissions: "Permisos",
            select_destination_folder: "Seleccione la carpeta de destino",
            source: "Origen",
            destination: "Destino",
            copy_file: "Copiar archivo",
            sure_to_delete: "Esta seguro que desea eliminar",
            change_name_move: "Renombrar / mover",
            enter_new_name_for: "Ingrese el nuevo nombre para",
            extract_item: "Extraer archivo",
            extraction_started: "La extraccion ha comenzado en un proceso de segundo plano",
            compression_started: "La compresion ha comenzado en un proceso de segundo plano",
            enter_folder_name_for_extraction: "Ingrese el nombre de la carpeta para la extraccion de",
            enter_file_name_for_compression: "Ingrese el nombre del archivo para la compresion de",
            toggle_fullscreen: "Activar/Desactivar pantalla completa",
            edit_file: "Editar archivo",
            file_content: "Contenido del archivo",
            loading: "Cargando",
            search: "Buscar",
            create_folder: "Crear carpeta",
            create: "Crear",
            folder_name: "Nombre de la carpeta",
            upload: "Subir",
            change_permissions: "Cambiar permisos",
            change: "Cambiar",
            details: "Detalles",
            icons: "Iconos",
            list: "Lista",
            name: "Nombre",
            size: "Tamaño",
            actions: "Acciones",
            date: "Fecha",
            no_files_in_folder: "No hay archivos en esta carpeta",
            no_folders_in_folder: "Esta carpeta no contiene sub-carpetas",
            select_this: "Seleccionar esta",
            go_back: "Volver",
            wait: "Espere",
            move: "Mover",
            download: "Descargar",
            view_item: "Ver archivo",
            remove: "Eliminar",
            edit: "Editar",
            copy: "Copiar",
            rename: "Renombrar",
            extract: "Extraer",
            compress: "Comprimir",
            error_invalid_filename: "El nombre del archivo es invalido o ya existe",
            error_modifying: "Ocurrio un error al intentar modificar el archivo",
            error_deleting: "Ocurrio un error al intentar eliminar el archivo",
            error_renaming: "Ocurrio un error al intentar renombrar el archivo",
            error_copying: "Ocurrio un error al intentar copiar el archivo",
            error_compressing: "Ocurrio un error al intentar comprimir el archivo",
            error_extracting: "Ocurrio un error al intentar extraer el archivo",
            error_creating_folder: "Ocurrio un error al intentar crear la carpeta",
            error_getting_content: "Ocurrio un error al obtener el contenido del archivo",
            error_changing_perms: "Ocurrio un error al cambiar los permisos del archivo",
            error_uploading_files: "Ocurrio un error al subir archivos",
            sure_to_start_compression_with: "Esta seguro que desea comprimir",
            owner: "Propietario",
            group: "Grupo",
            others: "Otros",
            read: "Lectura",
            write: "Escritura",
            exec: "Ejecucion",
            original: "Original",
            changes: "Cambios",
            recursive: "Recursivo",
            preview: "Vista previa",
            open: "Abrir",
            these_elements: "estos {{total}} elementos",
            new_folder: "Nueva carpeta",
            download_as_zip: "Descargar como ZIP"
        }), e.translations("fr", {
            filemanager: "Gestionnaire de fichier",
            language: "Langue",
            english: "Anglais",
            spanish: "Espagnol",
            portuguese: "Portugais",
            french: "Français",
            german: "Allemand",
            hebrew: "Hébreu",
            slovak: "Slovaque",
            chinese: "Chinois",
            russian: "Russe",
            ukrainian: "Ukrainien",
            turkish: "Turc",
            persian: "Persan",
            confirm: "Confirmer",
            cancel: "Annuler",
            close: "Fermer",
            upload_files: "Télécharger des fichiers",
            files_will_uploaded_to: "Les fichiers seront uploadé dans",
            select_files: "Sélectionnez les fichiers",
            uploading: "Upload en cours",
            permissions: "Permissions",
            select_destination_folder: "Sélectionné le dossier de destination",
            source: "Source",
            destination: "Destination",
            copy_file: "Copier le fichier",
            sure_to_delete: "Êtes-vous sûr de vouloir supprimer",
            change_name_move: "Renommer / Déplacer",
            enter_new_name_for: "Entrer le nouveau nom pour",
            extract_item: "Extraires les éléments",
            extraction_started: "L'extraction a démarré en tâche de fond",
            compression_started: "La compression a démarré en tâche de fond",
            enter_folder_name_for_extraction: "Entrer le nom du dossier pour l'extraction de",
            enter_file_name_for_compression: "Entrez le nom de fichier pour la compression de",
            toggle_fullscreen: "Basculer en plein écran",
            edit_file: "Éditer le fichier",
            file_content: "Contenu du fichier",
            loading: "Chargement en cours",
            search: "Recherche",
            create_folder: "Créer un dossier",
            create: "Créer",
            folder_name: "Nom du dossier",
            upload: "Upload",
            change_permissions: "Changer les permissions",
            change: "Changer",
            details: "Details",
            icons: "Icons",
            list: "Liste",
            name: "Nom",
            size: "Taille",
            actions: "Actions",
            date: "Date",
            no_files_in_folder: "Aucun fichier dans ce dossier",
            no_folders_in_folder: "Ce dossier ne contiens pas de dossier",
            select_this: "Sélectionner",
            go_back: "Retour",
            wait: "Patienter",
            move: "Déplacer",
            download: "Télécharger",
            view_item: "Voir l'élément",
            remove: "Supprimer",
            edit: "Éditer",
            copy: "Copier",
            rename: "Renommer",
            extract: "Extraire",
            compress: "Compresser",
            error_invalid_filename: "Nom de fichier invalide ou déjà existant, merci de spécifier un autre nom",
            error_modifying: "Une erreur est survenue pendant la modification du fichier",
            error_deleting: "Une erreur est survenue pendant la suppression du fichier ou du dossier",
            error_renaming: "Une erreur est survenue pendant le renommage du fichier",
            error_copying: "Une erreur est survenue pendant la copie du fichier",
            error_compressing: "Une erreur est survenue pendant la compression du fichier ou du dossier",
            error_extracting: "Une erreur est survenue pendant l'extraction du fichier",
            error_creating_folder: "Une erreur est survenue pendant la création du dossier",
            error_getting_content: "Une erreur est survenue pendant la récupération du contenu du fichier",
            error_changing_perms: "Une erreur est survenue pendant le changement des permissions du fichier",
            error_uploading_files: "Une erreur est survenue pendant l'upload des fichiers",
            sure_to_start_compression_with: "Êtes-vous sûre de vouloir compresser",
            owner: "Propriétaire",
            group: "Groupe",
            others: "Autres",
            read: "Lecture",
            write: "Écriture",
            exec: "Éxécution",
            original: "Original",
            changes: "Modifications",
            recursive: "Récursif",
            preview: "Aperçu",
            open: "Ouvrir",
            these_elements: "ces {{total}} éléments",
            new_folder: "Nouveau dossier",
            download_as_zip: "Télécharger comme ZIP"
        }), e.translations("de", {
            filemanager: "Dateimanager",
            language: "Sprache",
            english: "Englisch",
            spanish: "Spansisch",
            portuguese: "Portugiesisch",
            french: "Französisch",
            german: "Deutsch",
            hebrew: "Hebräisch",
            slovak: "Slowakisch",
            chinese: "Chinesisch",
            russian: "Russisch",
            ukrainian: "Ukrainisch",
            turkish: "Türkisch",
            persian: "Persisch",
            confirm: "Bestätigen",
            cancel: "Abbrechen",
            close: "Schließen",
            upload_files: "Hochladen von vateien",
            files_will_uploaded_to: "Dateien werden hochgeladen nach",
            select_files: "Wählen Sie die Dateien",
            uploading: "Lade hoch",
            permissions: "Berechtigungen",
            select_destination_folder: "Wählen Sie einen Zielordner",
            source: "Quelle",
            destination: "Ziel",
            copy_file: "Datei kopieren",
            sure_to_delete: "Sind Sie sicher, dass Sie die Datei löschen möchten?",
            change_name_move: "Namen ändern / verschieben",
            enter_new_name_for: "Geben Sie den neuen Namen ein für",
            extract_item: "Archiv entpacken",
            extraction_started: "Entpacken hat im Hintergrund begonnen",
            compression_started: "Komprimierung hat im Hintergrund begonnen",
            enter_folder_name_for_extraction: "Geben sie den verzeichnisnamen für die entpackung an, von",
            enter_file_name_for_compression: "Geben sie den dateinamen für die kompression von",
            toggle_fullscreen: "Vollbild umschalten",
            edit_file: "Datei bearbeiten",
            file_content: "Dateiinhalt",
            loading: "Lade",
            search: "Suche",
            create_folder: "Ordner erstellen",
            create: "Erstellen",
            folder_name: "Verzeichnisname",
            upload: "Hochladen",
            change_permissions: "Berechtigungen ändern",
            change: "Ändern",
            details: "Details",
            icons: "Symbolansicht",
            list: "Listenansicht",
            name: "Name",
            size: "Größe",
            actions: "Aktionen",
            date: "Datum",
            no_files_in_folder: "Keine Dateien in diesem Ordner",
            no_folders_in_folder: "Dieser Ordner enthält keine Unterordner",
            select_this: "Auswählen",
            go_back: "Zurück",
            wait: "Warte",
            move: "Verschieben",
            download: "Herunterladen",
            view_item: "Datei ansehen",
            remove: "Löschen",
            edit: "Bearbeiten",
            copy: "Kopieren",
            rename: "Umbenennen",
            extract: "Entpacken",
            compress: "Komprimieren",
            error_invalid_filename: "Ungültiger Dateiname oder existiert bereits",
            error_modifying: "Beim Bearbeiten der Datei ist ein Fehler aufgetreten",
            error_deleting: "Beim Löschen der Datei oder des Ordners ist ein Fehler aufgetreten",
            error_renaming: "Beim Umbennenen der Datei ist ein Fehler aufgetreten",
            error_copying: "Beim Kopieren der Datei ist ein Fehler aufgetreten",
            error_compressing: "Beim Komprimieren der Datei oder des Ordners ist ein Fehler aufgetreten",
            error_extracting: "Beim Entpacken der Datei ist ein Fehler aufgetreten",
            error_creating_folder: "Beim Erstellen des Ordners ist ein Fehler aufgetreten",
            error_getting_content: "Beim Holen des Dateiinhalts ist ein Fehler aufgetreten",
            error_changing_perms: "Beim Ändern der Dateiberechtigungen ist ein Fehler aufgetreten",
            error_uploading_files: "Beim Hochladen der Dateien ist ein Fehler aufgetreten",
            sure_to_start_compression_with: "Möchten Sie die Datei wirklich komprimieren?",
            owner: "Besitzer",
            group: "Gruppe",
            others: "Andere",
            read: "Lesen",
            write: "Schreiben",
            exec: "Ausführen",
            original: "Original",
            changes: "Änderungen",
            recursive: "Rekursiv",
            preview: "Dateivorschau",
            open: "Öffnen",
            these_elements: "diese {{total}} elemente",
            new_folder: "Neuer ordner",
            download_as_zip: "Download als ZIP"
        }), e.translations("sk", {
            filemanager: "Správca súborov",
            language: "Jazyk",
            english: "Angličtina",
            spanish: "Španielčina",
            portuguese: "Portugalčina",
            french: "Francúzština",
            german: "Nemčina",
            hebrew: "Hebrejčina",
            slovak: "Slovenčina",
            chinese: "Čínština",
            russian: "Ruský",
            ukrainian: "Ukrajinský",
            turkish: "Turecký",
            persian: "Perzský",
            confirm: "Potvrdiť",
            cancel: "Zrušiť",
            close: "Zavrieť",
            upload_files: "Nahrávať súbory",
            files_will_uploaded_to: "Súbory budú nahrané do",
            select_files: "Vybrať súbory",
            uploading: "Nahrávanie",
            permissions: "Oprávnenia",
            select_destination_folder: "Vyberte cieľový príečinok",
            source: "Zdroj",
            destination: "Cieľ",
            copy_file: "Kopírovať súbor",
            sure_to_delete: "Ste si istý, že chcete vymazať",
            change_name_move: "Premenovať / Premiestniť",
            enter_new_name_for: "Zadajte nové meno pre",
            extract_item: "Rozbaliť položku",
            extraction_started: "Rozbaľovanie začalo v procese na pozadí",
            compression_started: "Kompresia začala v procese na pzoadí",
            enter_folder_name_for_extraction: "Zadajte názov priečinka na rozbalenie",
            enter_file_name_for_compression: "Zadajte názov súboru pre kompresiu",
            toggle_fullscreen: "Prepnúť režim na celú obrazovku",
            edit_file: "Upraviť súbor",
            file_content: "Obsah súboru",
            loading: "Načítavanie",
            search: "Hľadať",
            create_folder: "Vytvoriť priečinok",
            create: "Vytvoriť",
            folder_name: "Názov priećinka",
            upload: "Nahrať",
            change_permissions: "Zmeniť oprávnenia",
            change: "Zmeniť",
            details: "Podrobnosti",
            icons: "Ikony",
            list: "Zoznam",
            name: "Meno",
            size: "Veľkosť",
            actions: "Akcie",
            date: "Dátum",
            no_files_in_folder: "V tom to priečinku nie sú žiadne súbory",
            no_folders_in_folder: "Tento priečinok neobsahuje žiadne ďalšie priećinky",
            select_this: "Vybrať tento",
            go_back: "Ísť späť",
            wait: "Počkajte",
            move: "Presunúť",
            download: "Stiahnuť",
            view_item: "Zobraziť položku",
            remove: "Vymazať",
            edit: "Upraviť",
            copy: "Kopírovať",
            rename: "Premenovať",
            extract: "Rozbaliť",
            compress: "Komprimovať",
            error_invalid_filename: "Neplatné alebo duplicitné meno súboru, vyberte iné meno",
            error_modifying: "Vyskytla sa chyba pri upravovaní súboru",
            error_deleting: "Vyskytla sa chyba pri mazaní súboru alebo priečinku",
            error_renaming: "Vyskytla sa chyba pri premenovaní súboru",
            error_copying: "Vyskytla sa chyba pri kopírovaní súboru",
            error_compressing: "Vyskytla sa chyba pri komprimovaní súboru alebo priečinka",
            error_extracting: "Vyskytla sa chyba pri rozbaľovaní súboru",
            error_creating_folder: "Vyskytla sa chyba pri vytváraní priečinku",
            error_getting_content: "Vyskytla sa chyba pri získavaní obsahu súboru",
            error_changing_perms: "Vyskytla sa chyba pri zmene oprávnení súboru",
            error_uploading_files: "Vyskytla sa chyba pri nahrávaní súborov",
            sure_to_start_compression_with: "Ste si istý, že chcete komprimovať",
            owner: "Vlastník",
            group: "Skupina",
            others: "Ostatní",
            read: "Čítanie",
            write: "Zapisovanie",
            exec: "Spúštanie",
            original: "Originál",
            changes: "Zmeny",
            recursive: "Rekurzívne",
            preview: "Náhľad položky",
            open: "Otvoriť",
            these_elements: "týchto {{total}} prvkov",
            new_folder: "Nový priečinok",
            download_as_zip: "Stiahnuť ako ZIP"
        }), e.translations("zh", {
            filemanager: "文档管理器",
            language: "语言",
            english: "英语",
            spanish: "西班牙语",
            portuguese: "葡萄牙语",
            french: "法语",
            german: "德语",
            hebrew: "希伯来语",
            slovak: "斯洛伐克语",
            chinese: "中文",
            russian: "俄語",
            ukrainian: "烏克蘭",
            turkish: "土耳其",
            persian: "波斯語",
            confirm: "确定",
            cancel: "取消",
            close: "关闭",
            upload_files: "上传文件",
            files_will_uploaded_to: "文件将上传到",
            select_files: "选择文件",
            uploading: "上传中",
            permissions: "权限",
            select_destination_folder: "选择目标文件",
            source: "源自",
            destination: "目的地",
            copy_file: "复制文件",
            sure_to_delete: "确定要删除？",
            change_name_move: "改名或移动？",
            enter_new_name_for: "输入新的名称",
            extract_item: "解压",
            extraction_started: "解压已经在后台开始",
            compression_started: "压缩已经在后台开始",
            enter_folder_name_for_extraction: "输入解压的目标文件夹",
            enter_file_name_for_compression: "输入要压缩的文件名",
            toggle_fullscreen: "切换全屏",
            edit_file: "编辑文件",
            file_content: "文件内容",
            loading: "加载中",
            search: "搜索",
            create_folder: "创建文件夹",
            create: "创建",
            folder_name: "文件夹名称",
            upload: "上传",
            change_permissions: "修改权限",
            change: "修改",
            details: "详细信息",
            icons: "图标",
            list: "列表",
            name: "名称",
            size: "尺寸",
            actions: "操作",
            date: "日期",
            no_files_in_folder: "此文件夹没有文件",
            no_folders_in_folder: "此文件夹不包含子文件夹",
            select_this: "选择此文件",
            go_back: "后退",
            wait: "等待",
            move: "移动",
            download: "下载",
            view_item: "查看子项",
            remove: "删除",
            edit: "编辑",
            copy: "复制",
            rename: "重命名",
            extract: "解压",
            compress: "压缩",
            error_invalid_filename: "非法文件名或文件已经存在, 请指定其它名称",
            error_modifying: "修改文件出错",
            error_deleting: "删除文件或文件夹出错",
            error_renaming: "重命名文件出错",
            error_copying: "复制文件出错",
            error_compressing: "压缩文件或文件夹出错",
            error_extracting: "解压文件出错",
            error_creating_folder: "创建文件夹出错",
            error_getting_content: "获取文件内容出错",
            error_changing_perms: "修改文件权限出错",
            error_uploading_files: "上传文件出错",
            sure_to_start_compression_with: "确定要压缩？",
            owner: "拥有者",
            group: "群组",
            others: "其他",
            read: "读取",
            write: "写入",
            exec: "执行",
            original: "原始",
            changes: "变化",
            recursive: "递归",
            preview: "成员预览",
            open: "打开",
            these_elements: "共 {{total}} 个",
            new_folder: "新文件夹",
            download_as_zip: "下载的ZIP"
        }), e.translations("ru", {
            filemanager: "Файловый менеджер",
            language: "Язык",
            english: "Английский",
            spanish: "Испанский",
            portuguese: "Португальский",
            french: "Французкий",
            german: "Немецкий",
            hebrew: "Хинди",
            slovak: "Словацкий",
            chinese: "Китайский",
            russian: "русский",
            ukrainian: "украинец",
            turkish: "турецкий",
            persian: "персидский",
            confirm: "Подьвердить",
            cancel: "Отменить",
            close: "Закрыть",
            upload_files: "Загрузка файлов",
            files_will_uploaded_to: "Файлы будут загружены в: ",
            select_files: "Выберите файлы",
            uploading: "Загрузка",
            permissions: "Разрешения",
            select_destination_folder: "Выберите папку назначения",
            source: "Источкик",
            destination: "Цель",
            copy_file: "Скопировать файл",
            sure_to_delete: "Действительно удалить?",
            change_name_move: "Переименовать / переместить",
            enter_new_name_for: "Новое имя для",
            extract_item: "Извлечь",
            extraction_started: "Извлечение начато",
            compression_started: "Сжатие начато",
            enter_folder_name_for_extraction: "Извлечь в укананную папку",
            enter_file_name_for_compression: "Введите имя архива",
            toggle_fullscreen: "На весь экран",
            edit_file: "Редактировать",
            file_content: "Содержимое файла",
            loading: "Загрузка",
            search: "Поиск",
            create_folder: "Создать папку",
            create: "Создать",
            folder_name: "Имя папки",
            upload: "Загрузить",
            change_permissions: "Изменить разрешения",
            change: "Изменить",
            details: "Свойства",
            icons: "Иконки",
            list: "Список",
            name: "Имя",
            size: "Размер",
            actions: "Действия",
            date: "Дата",
            no_files_in_folder: "Пустая папка",
            no_folders_in_folder: "Пустая папка",
            select_this: "Выбрать",
            go_back: "Назад",
            wait: "Подождите",
            move: "Переместить",
            download: "Скачать",
            view_item: "Отобразить содержимое",
            remove: "Удалить",
            edit: "Редактировать",
            copy: "Скопировать",
            rename: "Переименовать",
            extract: "Извлечь",
            compress: "Сжать",
            error_invalid_filename: "Имя неверное или уже существует, выберите другое",
            error_modifying: "Произошла ошибка при модифицировании файла",
            error_deleting: "Произошла ошибка при удалении",
            error_renaming: "Произошла ошибка при переименовании файла",
            error_copying: "Произошла ошибка при копировании файла",
            error_compressing: "Произошла ошибка при сжатии",
            error_extracting: "Произошла ошибка при извлечении",
            error_creating_folder: "Произошла ошибка при создании папки",
            error_getting_content: "Произошла ошибка при получении содержимого",
            error_changing_perms: "Произошла ошибка при изменении разрешений",
            error_uploading_files: "Произошла ошибка при загрузке",
            sure_to_start_compression_with: "Действительно сжать",
            owner: "Владелец",
            group: "Группа",
            others: "Другие",
            read: "Чтение",
            write: "Запись",
            exec: "Выполнение",
            original: "По-умолчанию",
            changes: "Изменения",
            recursive: "Рекурсивно",
            preview: "Просмотр",
            open: "Открыть",
            these_elements: "всего {{total}} елементов",
            new_folder: "Новая папка",
            download_as_zip: "Download as ZIP"
        }), e.translations("ua", {
            filemanager: "Файловий менеджер",
            language: "Мова",
            english: "Англійська",
            spanish: "Іспанська",
            portuguese: "Португальська",
            french: "Французька",
            german: "Німецька",
            hebrew: "Хінді",
            slovak: "Словацька",
            chinese: "Китайська",
            russian: "російський",
            ukrainian: "український",
            turkish: "турецька",
            persian: "перський",
            confirm: "Підтвердити",
            cancel: "Відмінити",
            close: "Закрити",
            upload_files: "Завантаження файлів",
            files_will_uploaded_to: "Файли будуть завантажені у: ",
            select_files: "Виберіть файли",
            uploading: "Завантаження",
            permissions: "Дозволи",
            select_destination_folder: "Виберіть папку призначення",
            source: "Джерело",
            destination: "Ціль",
            copy_file: "Скопіювати файл",
            sure_to_delete: "Дійсно удалить?",
            change_name_move: "Перейменувати / перемістити",
            enter_new_name_for: "Нове ім'я для",
            extract_item: "Извлечь",
            extraction_started: "Извлечение начато",
            compression_started: "Архівацію почато",
            enter_folder_name_for_extraction: "Извлечь в укананную папку",
            enter_file_name_for_compression: "Введите имя архива",
            toggle_fullscreen: "На весь экран",
            edit_file: "Редагувати",
            file_content: "Вміст файлу",
            loading: "Завантаження",
            search: "Пошук",
            create_folder: "Створити папку",
            create: "Створити",
            folder_name: "Ім'я  папки",
            upload: "Завантижити",
            change_permissions: "Змінити дозволи",
            change: "Редагувати",
            details: "Властивості",
            icons: "Іконки",
            list: "Список",
            name: "Ім'я",
            size: "Розмір",
            actions: "Дії",
            date: "Дата",
            no_files_in_folder: "Пуста папка",
            no_folders_in_folder: "Пуста папка",
            select_this: "Выбрати",
            go_back: "Назад",
            wait: "Зачекайте",
            move: "Перемістити",
            download: "Скачати",
            view_item: "Показати вміст",
            remove: "Видалити",
            edit: "Редагувати",
            copy: "Копіювати",
            rename: "Переіменувати",
            extract: "Розархівувати",
            compress: "Архівувати",
            error_invalid_filename: "Ім'я певірне або вже існує, виберіть інше",
            error_modifying: "Виникла помилка при редагуванні файлу",
            error_deleting: "Виникла помилка при видаленні",
            error_renaming: "Виникла помилка при зміні імені файлу",
            error_copying: "Виникла помилка при коміюванні файлу",
            error_compressing: "Виникла помилка при стисненні",
            error_extracting: "Виникла помилка при розархівації",
            error_creating_folder: "Виникла помилка при створенні папки",
            error_getting_content: "Виникла помилка при отриманні вмісту",
            error_changing_perms: "Виникла помилка при зміні дозволів",
            error_uploading_files: "Виникла помилка при завантаженні",
            sure_to_start_compression_with: "Дійсно стиснути",
            owner: "Власник",
            group: "Група",
            others: "Інші",
            read: "Читання",
            write: "Запис",
            exec: "Виконання",
            original: "За замовчуванням",
            changes: "Зміни",
            recursive: "Рекурсивно",
            preview: "Перегляд",
            open: "Відкрити",
            these_elements: "усього {{total}} елементів",
            new_folder: "Нова папка",
            download_as_zip: "Download as ZIP"
        }), e.translations("tr", {
            filemanager: "Dosya Yöneticisi",
            language: "Dil",
            english: "İngilizce",
            spanish: "İspanyolca",
            portuguese: "Portekizce",
            french: "Fransızca",
            german: "Almanca",
            hebrew: "İbranice",
            slovak: "Slovakça",
            chinese: "Çince",
            russian: "Rusça",
            ukrainian: "Ukrayna",
            turkish: "Türk",
            persian: "Farsça",
            confirm: "Onayla",
            cancel: "İptal Et",
            close: "Kapat",
            upload_files: "Dosya yükle",
            files_will_uploaded_to: "Dosyalar yüklenecektir.",
            select_files: "Dosya Seç",
            uploading: "Yükleniyor",
            permissions: "İzinler",
            select_destination_folder: "Hedef klasör seçin",
            source: "Kaynak",
            destination: "Hedef",
            copy_file: "Dosyayı kopyala",
            sure_to_delete: "Silmek istediğinden emin misin",
            change_name_move: "İsmini değiştir / taşı",
            enter_new_name_for: "Yeni ad girin",
            extract_item: "Dosya çıkar",
            extraction_started: "Çıkarma işlemi arkaplanda devam ediyor",
            compression_started: "Sıkıştırma işlemi arkaplanda başladı",
            enter_folder_name_for_extraction: "Çıkarılması için klasör adı girin",
            enter_file_name_for_compression: "Sıkıştırılması için dosya adı girin",
            toggle_fullscreen: "Tam ekran moduna geç",
            edit_file: "Dosyayı düzenle",
            file_content: "Dosya içeriği",
            loading: "Yükleniyor",
            search: "Ara",
            create_folder: "Klasör oluştur",
            create: "Oluştur",
            folder_name: "Klasör adı",
            upload: "Yükle",
            change_permissions: "İzinleri değiştir",
            change: "Değiştir",
            details: "Detaylar",
            icons: "simgeler",
            list: "Liste",
            name: "Adı",
            size: "Boyutu",
            actions: "İşlemler",
            date: "Tarih",
            no_files_in_folder: "Klasörde hiç dosya yok",
            no_folders_in_folder: "Bu klasör alt klasör içermez",
            select_this: "Bunu seç",
            go_back: "Geri git",
            wait: "Bekle",
            move: "Taşı",
            download: "İndir",
            view_item: "Dosyayı görüntüle",
            remove: "Sil",
            edit: "Düzenle",
            copy: "Kopyala",
            rename: "Yeniden Adlandır",
            extract: "Çıkart",
            compress: "Sıkıştır",
            error_invalid_filename: "Geçersiz dosya adı, bu dosya adına sahip dosya mevcut",
            error_modifying: "Dosya düzenlenirken bir hata oluştu",
            error_deleting: "Klasör veya dosya silinirken bir hata oluştu",
            error_renaming: "Dosya yeniden adlandırılırken bir hata oluştu",
            error_copying: "Dosya kopyalanırken bir hata oluştu",
            error_compressing: "Dosya veya klasör sıkıştırılırken bir hata oluştu",
            error_extracting: "Çıkartılırken bir hata oluştu",
            error_creating_folder: "Klasör oluşturulurken bir hata oluştu",
            error_getting_content: "Dosya detayları alınırken bir hata oluştu",
            error_changing_perms: "Dosyanın izini değiştirilirken bir hata oluştu",
            error_uploading_files: "Dosyalar yüklenirken bir hata oluştu",
            sure_to_start_compression_with: "Sıkıştırmak istediğinden emin misin",
            owner: "Sahip",
            group: "Grup",
            others: "Diğerleri",
            read: "Okuma",
            write: "Yazma",
            exec: "Gerçekleştir",
            original: "Orjinal",
            changes: "Değişiklikler",
            recursive: "Yinemeli",
            preview: "Dosyayı önizle",
            open: "Aç",
            these_elements: "{{total}} eleman",
            new_folder: "Yeni Klasör",
            download_as_zip: "ZIP olarak indir"
        }), e.translations("fa", {
            filemanager: "مدیریت فایل ها",
            language: "زبان",
            english: "انگلیسی",
            spanish: "اسپانیایی",
            portuguese: "پرتغالی",
            french: "فرانسه",
            german: "آلمانی",
            hebrew: "عبری",
            slovak: "اسلواک",
            chinese: "چینی",
            russian: "روسی",
            ukrainian: "اوکراینی",
            turkish: "ترکی",
            persian: "فارسی",
            confirm: "تایید",
            cancel: "رد",
            close: "بستن",
            upload_files: "آپلود فایل",
            files_will_uploaded_to: "فایل ها آپلود می شوند به",
            select_files: "انتخاب فایل ها",
            uploading: "در حال آپلود",
            permissions: "مجوز ها",
            select_destination_folder: "پوشه مقصد را انتخاب کنید",
            source: "مبدا",
            destination: "مقصد",
            copy_file: "کپی فایل",
            sure_to_delete: "مطمين هستید می خواهید حذف کنید؟",
            change_name_move: "تغییر نام و جابجایی",
            enter_new_name_for: "نام جدیدی وارد کنید برای",
            extract_item: "خارج کردن از حالت فشرده",
            extraction_started: "یک پروسه در پس زمینه شروع به خارج کردن از حالت فشرده کرد",
            compression_started: "یک پروسه در پس زمینه شروع به فشرده سازی کرد",
            enter_folder_name_for_extraction: "نام پوشه مقصد برای خارج کردن از حالت فشرده را وارد کنید",
            enter_file_name_for_compression: "نام پوشه مقصد برای فشرده سازی را وارد کنید",
            toggle_fullscreen: "تعویض حالت تمام صفحه",
            edit_file: "ویرایش",
            file_content: "محتویات",
            loading: "در حال بارگذاری",
            search: "جستجو",
            create_folder: "پوشه جدید",
            create: "ساختن",
            folder_name: "نام پوشه",
            upload: "آپلود",
            change_permissions: "تغییر مجوز ها",
            change: "تغییر",
            details: "جزییات",
            icons: "آیکون ها",
            list: "لیست",
            name: "نام",
            size: "سایز",
            actions: "اعمال",
            date: "تاریخ",
            no_files_in_folder: "هیچ فایلی در این پوشه نیست",
            no_folders_in_folder: "هیچ پوشه ای داخل این پوشه قرار ندارد",
            select_this: "انتخاب",
            go_back: "بازگشت",
            wait: "منتظر بمانید",
            move: "جابجایی",
            download: "دانلود",
            view_item: "مشاهده این مورد",
            remove: "حذف",
            edit: "ویرایش",
            copy: "کپی",
            rename: "تغییر نام",
            extract: "خروج از حالت فشرده",
            compress: "فشرده سازی",
            error_invalid_filename: "نام فایل مورد درست نیست و یا قبلا استفاده شده است، لطفا نام دیگری وارد کنید",
            error_modifying: "در هنگام تغییر فایل خطایی پیش آمد",
            error_deleting: "در هنگام حذف فایل خطایی پیش آمد",
            error_renaming: "در هنگام تغییر نام فایل خطایی پیش آمد",
            error_copying: "در هنگام کپی کردن فایل خطایی پیش آمد",
            error_compressing: "در هنگام فشرده سازی فایل خطایی پیش آمد",
            error_extracting: "در هنگام خارک کردن فایل از حالت فشرده خطایی پیش آمد",
            error_creating_folder: "در هنگام ساخت پوشه خطایی پیش امد",
            error_getting_content: "در هنگام بارگذاری محتویات فایل خطایی رخ داد",
            error_changing_perms: "در هنگام تغییر مجوز های فایل خطایی رخ داد",
            error_uploading_files: "در آپلود فایل خطایی رخ داد",
            sure_to_start_compression_with: "مطمئن هستید فشرده سازی انجام شد؟",
            owner: "مالک فایل",
            group: "گروه",
            others: "دیگران",
            read: "خواندن",
            write: "نوشتن",
            exec: "اجرا کردن",
            original: "اصلی",
            changes: "تغییرات",
            recursive: "بازگشتی",
            preview: "پیش نمایش",
            open: "باز کردن",
            these_elements: "تعداد {{total}} مورد",
            new_folder: "پوشه جدید",
            download_as_zip: "به عنوان فایل فشرده دانلود شود"
        })
    }])
}(angular), angular.module("FileManagerApp").run(["$templateCache", function(e) {
    e.put("src/templates/current-folder-breadcrumb.html", '<ol class="breadcrumb">\r\n    <li>\r\n        <a href="" ng-click="fileNavigator.goTo(-1)">\r\n            {{ config.appName }}\r\n        </a>\r\n    </li>\r\n    <li ng-repeat="(key, dir) in fileNavigator.currentPath track by key" ng-class="{\'active\':$last}" class="animated fast fadeIn">\r\n        <a href="" ng-show="!$last" ng-click="fileNavigator.goTo(key)">\r\n            {{dir | strLimit : 8}}\r\n        </a>\r\n        <span ng-show="$last">\r\n            {{dir | strLimit : 12}}\r\n        </span>\r\n    </li>\r\n</ol>'), e.put("src/templates/item-context-menu.html", '<div id="context-menu" class="dropdown clearfix animated fast fadeIn">\r\n    <ul class="dropdown-menu dropdown-right-click" role="menu" aria-labelledby="dropdownMenu" ng-show="temps.length">\r\n\r\n        <li ng-show="singleSelection() && singleSelection().isFolder()">\r\n            <a href="" tabindex="-1" ng-click="smartClick(singleSelection())">\r\n                <i class="glyphicon glyphicon-folder-open"></i> {{\'open\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.pickCallback && singleSelection() && singleSelection().isSelectable()">\r\n            <a href="" tabindex="-1" ng-click="config.pickCallback(singleSelection().model)">\r\n                <i class="glyphicon glyphicon-hand-up"></i> {{\'select_this\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.download && !selectionHas(\'dir\') && singleSelection()">\r\n            <a href="" tabindex="-1" ng-click="download()">\r\n                <i class="glyphicon glyphicon-cloud-download"></i> {{\'download\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.downloadMultiple && !selectionHas(\'dir\') && !singleSelection()">\r\n            <a href="" tabindex="-1" ng-click="download()">\r\n                <i class="glyphicon glyphicon-cloud-download"></i> {{\'download_as_zip\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.preview && singleSelection().isImage() && singleSelection()">\r\n            <a href="" tabindex="-1" ng-click="openImagePreview()">\r\n                <i class="glyphicon glyphicon-picture"></i> {{\'view_item\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.rename && singleSelection()">\r\n            <a href="" tabindex="-1" ng-click="modal(\'rename\')">\r\n                <i class="glyphicon glyphicon-edit"></i> {{\'rename\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.move">\r\n            <a href="" tabindex="-1" ng-click="modalWithPathSelector(\'move\')">\r\n                <i class="glyphicon glyphicon-arrow-right"></i> {{\'move\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.copy && !selectionHas(\'dir\')">\r\n            <a href="" tabindex="-1" ng-click="modalWithPathSelector(\'copy\')">\r\n                <i class="glyphicon glyphicon-log-out"></i> {{\'copy\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.edit && singleSelection() && singleSelection().isEditable()">\r\n            <a href="" tabindex="-1" ng-click="openEditItem()">\r\n                <i class="glyphicon glyphicon-pencil"></i> {{\'edit\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.changePermissions">\r\n            <a href="" tabindex="-1" ng-click="modal(\'changepermissions\')">\r\n                <i class="glyphicon glyphicon-lock"></i> {{\'permissions\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.compress && (!singleSelection() || selectionHas(\'dir\'))">\r\n            <a href="" tabindex="-1" ng-click="modal(\'compress\')">\r\n                <i class="glyphicon glyphicon-compressed"></i> {{\'compress\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li ng-show="config.allowedActions.extract && singleSelection() && singleSelection().isExtractable()">\r\n            <a href="" tabindex="-1" ng-click="modal(\'extract\')">\r\n                <i class="glyphicon glyphicon-export"></i> {{\'extract\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n        <li class="divider" ng-show="config.allowedActions.remove"></li>\r\n        \r\n        <li ng-show="config.allowedActions.remove">\r\n            <a href="" tabindex="-1" ng-click="modal(\'remove\')">\r\n                <i class="glyphicon glyphicon-trash"></i> {{\'remove\' | translate}}\r\n            </a>\r\n        </li>\r\n\r\n    </ul>\r\n\r\n    <ul class="dropdown-menu dropdown-right-click" role="menu" aria-labelledby="dropdownMenu" ng-show="!temps.length">\r\n        <li ng-show="config.allowedActions.createFolder">\r\n            <a href="" tabindex="-1" ng-click="modal(\'newfolder\') && prepareNewFolder()">\r\n                <i class="glyphicon glyphicon-plus"></i> {{\'new_folder\' | translate}}\r\n            </a>\r\n        </li>\r\n        <li ng-show="config.allowedActions.upload">\r\n            <a href="" tabindex="-1" ng-click="modal(\'uploadfile\')">\r\n                <i class="glyphicon glyphicon-cloud-upload"></i> {{\'upload_files\' | translate}}\r\n            </a>\r\n        </li>\r\n    </ul>\r\n</div>'),
        e.put("src/templates/main-icons.html", '<div class="iconset noselect">\r\n    <div class="item-list clearfix" ng-click="selectOrUnselect(null, $event)" ng-right-click="selectOrUnselect(null, $event)" prevent="true">\r\n        <div class="col-120" ng-repeat="item in $parent.fileList = (fileNavigator.fileList | filter: {model:{name: query}})" ng-show="!fileNavigator.requesting && !fileNavigator.error">\r\n            <a href="" class="thumbnail text-center" ng-click="selectOrUnselect(item, $event)" ng-dblclick="smartClick(item)" ng-right-click="selectOrUnselect(item, $event)" title="{{item.model.name}} ({{item.model.size | humanReadableFileSize}})" ng-class="{selected: isSelected(item)}">\r\n                <div class="item-icon">\r\n                    <i class="glyphicon glyphicon-folder-open" ng-show="item.model.type === \'dir\'"></i>\r\n                    <i class="glyphicon glyphicon-file" data-ext="{{ item.model.name | fileExtension }}" ng-show="item.model.type === \'file\'" ng-class="{\'item-extension\': config.showExtensionIcons}"></i>\r\n                </div>\r\n                {{item.model.name | strLimit : 11 }}\r\n            </a>\r\n        </div>\r\n    </div>\r\n\r\n    <div ng-show="fileNavigator.requesting">\r\n        <div ng-include="config.tplPath + \'/spinner.html\'"></div>\r\n    </div>\r\n\r\n    <div class="alert alert-warning" ng-show="!fileNavigator.requesting && fileNavigator.fileList.length < 1 && !fileNavigator.error">\r\n        {{"no_files_in_folder" | translate}}...\r\n    </div>\r\n    \r\n    <div class="alert alert-danger" ng-show="!fileNavigator.requesting && fileNavigator.error">\r\n        {{ fileNavigator.error }}\r\n    </div>\r\n</div>'), e.put("src/templates/main-table-modal.html", '<table class="table table-condensed table-modal-condensed mb0">\r\n    <thead>\r\n        <tr>\r\n            <th>\r\n                <a href="" ng-click="order(\'model.name\')">\r\n                    {{"name" | translate}}\r\n                    <span class="sortorder" ng-show="predicate[1] === \'model.name\'" ng-class="{reverse:reverse}"></span>\r\n                </a>\r\n            </th>\r\n            <th class="text-right"></th>\r\n        </tr>\r\n    </thead>\r\n    <tbody class="file-item">\r\n        <tr ng-show="fileNavigator.requesting">\r\n            <td colspan="2">\r\n                <div ng-include="config.tplPath + \'/spinner.html\'"></div>\r\n            </td>\r\n        </tr>\r\n        <tr ng-show="!fileNavigator.requesting && !fileNavigator.listHasFolders() && !fileNavigator.error">\r\n            <td>\r\n                {{"no_folders_in_folder" | translate}}...\r\n            </td>\r\n            <td class="text-right">\r\n                <button class="btn btn-sm btn-default" ng-click="fileNavigator.upDir()">{{"go_back" | translate}}</button>\r\n            </td>\r\n        </tr>\r\n        <tr ng-show="!fileNavigator.requesting && fileNavigator.error">\r\n            <td colspan="2">\r\n                {{ fileNavigator.error }}\r\n            </td>\r\n        </tr>\r\n        <tr ng-repeat="item in fileNavigator.fileList | orderBy:predicate:reverse" ng-show="!fileNavigator.requesting && item.model.type === \'dir\'" ng-if="!selectedFilesAreChildOfPath(item)">\r\n            <td>\r\n                <a href="" ng-click="fileNavigator.folderClick(item)" title="{{item.model.name}} ({{item.model.size | humanReadableFileSize}})">\r\n                    <i class="glyphicon glyphicon-folder-close"></i>\r\n                    {{item.model.name | strLimit : 32}}\r\n                </a>\r\n            </td>\r\n            <td class="text-right">\r\n                <button class="btn btn-sm btn-default" ng-click="select(item)">\r\n                    <i class="glyphicon glyphicon-hand-up"></i> {{"select_this" | translate}}\r\n                </button>\r\n            </td>\r\n        </tr>\r\n    </tbody>\r\n</table>'), e.put("src/templates/main-table.html", '<table class="table mb0 table-files noselect">\r\n    <thead>\r\n        <tr>\r\n            <th>\r\n                <a href="" ng-click="order(\'model.name\')">\r\n                    {{"name" | translate}}\r\n                    <span class="sortorder" ng-show="predicate[1] === \'model.name\'" ng-class="{reverse:reverse}"></span>\r\n                </a>\r\n            </th>\r\n            <th class="hidden-xs" ng-hide="config.hideSize">\r\n                <a href="" ng-click="order(\'model.size\')">\r\n                    {{"size" | translate}}\r\n                    <span class="sortorder" ng-show="predicate[1] === \'model.size\'" ng-class="{reverse:reverse}"></span>\r\n                </a>\r\n            </th>\r\n            <th class="hidden-sm hidden-xs" ng-hide="config.hideDate">\r\n                <a href="" ng-click="order(\'model.date\')">\r\n                    {{"date" | translate}}\r\n                    <span class="sortorder" ng-show="predicate[1] === \'model.date\'" ng-class="{reverse:reverse}"></span>\r\n                </a>\r\n            </th>\r\n            <th class="hidden-sm hidden-xs" ng-hide="config.hidePermissions">\r\n                <a href="" ng-click="order(\'model.permissions\')">\r\n                    {{"permissions" | translate}}\r\n                    <span class="sortorder" ng-show="predicate[1] === \'model.permissions\'" ng-class="{reverse:reverse}"></span>\r\n                </a>\r\n            </th>\r\n        </tr>\r\n    </thead>\r\n    <tbody class="file-item">\r\n        <tr ng-show="fileNavigator.requesting">\r\n            <td colspan="5">\r\n                <div ng-include="config.tplPath + \'/spinner.html\'"></div>\r\n            </td>\r\n        </tr>\r\n        <tr ng-show="!fileNavigator.requesting &amp;&amp; fileNavigator.fileList.length < 1 &amp;&amp; !fileNavigator.error">\r\n            <td colspan="5">\r\n                {{"no_files_in_folder" | translate}}...\r\n            </td>\r\n        </tr>\r\n        <tr ng-show="!fileNavigator.requesting &amp;&amp; fileNavigator.error">\r\n            <td colspan="5">\r\n                {{ fileNavigator.error }}\r\n            </td>\r\n        </tr>\r\n        <tr class="item-list" ng-repeat="item in $parent.fileList = (fileNavigator.fileList | filter: {model:{name: query}} | orderBy:predicate:reverse)" ng-show="!fileNavigator.requesting" ng-click="selectOrUnselect(item, $event)" ng-dblclick="smartClick(item)" ng-right-click="selectOrUnselect(item, $event)" ng-class="{selected: isSelected(item)}">\r\n            <td>\r\n                <a class="fileselector" href="" title="{{item.model.name}} ({{item.model.size | humanReadableFileSize}})">\r\n                    <i class="glyphicon glyphicon-folder-close" ng-show="item.model.type === \'dir\'"></i>\r\n                    <i class="glyphicon glyphicon-file" ng-show="item.model.type === \'file\'"></i>\r\n                    {{item.model.name | strLimit : 64}}\r\n                </a>\r\n            </td>\r\n            <td class="hidden-xs ng-hide">\r\n                <span ng-show="item.model.type !== \'dir\' || config.showSizeForDirectories">\r\n                    {{item.model.size | humanReadableFileSize}}\r\n                </span>\r\n            </td>\r\n            <td class="hidden-sm hidden-xs" ng-hide="config.hideDate">\r\n                {{item.model.date | formatDate }}\r\n            </td>\r\n            <td class="hidden-sm hidden-xs" ng-hide="config.hidePermissions">\r\n                {{item.model.perms.toCode(item.model.type === \'dir\'?\'d\':\'-\')}}\r\n            </td>\r\n        </tr>\r\n    </tbody>\r\n</table>\r\n'), 
		e.put("src/templates/main.html", '<div ng-controller="FileManagerCtrl" ngf-drop="addForUpload($files)" ngf-drag-over-class="\'upload-dragover\'" ngf-multiple="true">\r\n    <div ng-include="config.tplPath + \'/navbar.html\'"></div>\r\n\r\n    <div class="container-fluid">\r\n        <div class="row">\r\n\r\n            <div class="col-sm-4 col-md-3 sidebar file-tree animated slow fadeIn" ng-include="config.tplPath + \'/sidebar.html\'" ng-show="config.sidebar &amp;&amp; fileNavigator.history[0]">\r\n            </div>\r\n\r\n            <div class="main" ng-class="config.sidebar &amp;&amp; fileNavigator.history[0] &amp;&amp; \'col-sm-8 col-md-9\'">\r\n                <div ng-include="config.tplPath +\'/main-table.html\'" class="main-navigation clearfix"></div>\r\n            </div>\r\n        </div>\r\n    </div>\r\n\r\n    <div ng-include="config.tplPath + \'/modals.html\'"></div>\r\n    <div ng-include="config.tplPath + \'/item-context-menu.html\'"></div>\r\n</div>\r\n'), e.put("src/templates/modals.html", '<div class="modal animated fadeIn" id="imagepreview">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n      <div class="modal-header">\r\n        <button type="button" class="close" data-dismiss="modal">\r\n            <span aria-hidden="true">&times;</span>\r\n            <span class="sr-only">{{"close" | translate}}</span>\r\n        </button>\r\n        <h4 class="modal-title">{{"preview" | translate}}</h4>\r\n      </div>\r\n      <div class="modal-body">\r\n        <div class="text-center">\r\n          <img id="imagepreview-target" class="preview" alt="{{singleSelection().model.name}}" ng-class="{\'loading\': apiMiddleware.apiHandler.inprocess}">\r\n          <span class="label label-warning" ng-show="apiMiddleware.apiHandler.inprocess">{{\'loading\' | translate}} ...</span>\r\n        </div>\r\n        <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n      </div>\r\n      <div class="modal-footer">\r\n        <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"close" | translate}}</button>\r\n      </div>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="remove">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n    <form ng-submit="remove()">\r\n      <div class="modal-header">\r\n        <button type="button" class="close" data-dismiss="modal">\r\n            <span aria-hidden="true">&times;</span>\r\n            <span class="sr-only">{{"close" | translate}}</span>\r\n        </button>\r\n        <h4 class="modal-title">{{"confirm" | translate}}</h4>\r\n      </div>\r\n      <div class="modal-body">\r\n        {{\'sure_to_delete\' | translate}} <span ng-include data-src="\'selected-files-msg\'"></span>\r\n\r\n        <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n      </div>\r\n      <div class="modal-footer">\r\n        <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>\r\n        <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess" autofocus="autofocus">{{"remove" | translate}}</button>\r\n      </div>\r\n      </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="move">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n        <form ng-submit="move()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{\'move\' | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n              <div ng-include data-src="\'path-selector\'" class="clearfix"></div>\r\n              <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>\r\n              <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{\'move\' | translate}}</button>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n\r\n<div class="modal animated fadeIn" id="rename">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n        <form ng-submit="rename()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{\'rename\' | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n              <label class="radio">{{\'enter_new_name_for\' | translate}} <b>{{singleSelection() && singleSelection().model.name}}</b></label>\r\n              <input class="form-control" ng-model="singleSelection().tempModel.name" autofocus="autofocus">\r\n\r\n              <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>\r\n              <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{\'rename\' | translate}}</button>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="copy">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n        <form ng-submit="copy()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{\'copy_file\' | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n              <div ng-show="singleSelection()">\r\n                <label class="radio">{{\'enter_new_name_for\' | translate}} <b>{{singleSelection().model.name}}</b></label>\r\n                <input class="form-control" ng-model="singleSelection().tempModel.name" autofocus="autofocus">\r\n              </div>\r\n\r\n              <div ng-include data-src="\'path-selector\'" class="clearfix"></div>\r\n              <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>\r\n              <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"copy" | translate}}</button>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="compress">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n        <form ng-submit="compress()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{\'compress\' | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n              <div ng-show="apiMiddleware.apiHandler.asyncSuccess">\r\n                  <div class="label label-success error-msg">{{\'compression_started\' | translate}}</div>\r\n              </div>\r\n              <div ng-hide="apiMiddleware.apiHandler.asyncSuccess">\r\n                  <div ng-hide="config.allowedActions.compressChooseName">\r\n                    {{\'sure_to_start_compression_with\' | translate}} <b>{{singleSelection().model.name}}</b> ?\r\n                  </div>\r\n                  <div ng-show="config.allowedActions.compressChooseName">\r\n                    <label class="radio">\r\n                      {{\'enter_file_name_for_compression\' | translate}}\r\n                      <span ng-include data-src="\'selected-files-msg\'"></span>\r\n                    </label>\r\n                    <input class="form-control" ng-model="temp.tempModel.name" autofocus="autofocus">\r\n                  </div>\r\n              </div>\r\n\r\n              <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <div ng-show="apiMiddleware.apiHandler.asyncSuccess">\r\n                  <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"close" | translate}}</button>\r\n              </div>\r\n              <div ng-hide="apiMiddleware.apiHandler.asyncSuccess">\r\n                  <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>\r\n                  <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{\'compress\' | translate}}</button>\r\n              </div>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="extract" ng-init="singleSelection().emptyName()">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n        <form ng-submit="extract()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{\'extract_item\' | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n              <div ng-show="apiMiddleware.apiHandler.asyncSuccess">\r\n                  <div class="label label-success error-msg">{{\'extraction_started\' | translate}}</div>\r\n              </div>\r\n              <div ng-hide="apiMiddleware.apiHandler.asyncSuccess">\r\n                  <label class="radio">{{\'enter_folder_name_for_extraction\' | translate}} <b>{{singleSelection().model.name}}</b></label>\r\n                  <input class="form-control" ng-model="singleSelection().tempModel.name" autofocus="autofocus">\r\n              </div>\r\n              <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <div ng-show="apiMiddleware.apiHandler.asyncSuccess">\r\n                  <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"close" | translate}}</button>\r\n              </div>\r\n              <div ng-hide="apiMiddleware.apiHandler.asyncSuccess">\r\n                  <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>\r\n                  <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{\'extract\' | translate}}</button>\r\n              </div>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="edit" ng-class="{\'modal-fullscreen\': fullscreen}">\r\n  <div class="modal-dialog modal-lg">\r\n    <div class="modal-content">\r\n        <form ng-submit="edit()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <button type="button" class="close fullscreen" ng-click="fullscreen=!fullscreen">\r\n                  <i class="glyphicon glyphicon-fullscreen"></i>\r\n                  <span class="sr-only">{{\'toggle_fullscreen\' | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{\'edit_file\' | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n                <label class="radio bold">{{ singleSelection().model.fullPath() }}</label>\r\n                <span class="label label-warning" ng-show="apiMiddleware.apiHandler.inprocess">{{\'loading\' | translate}} ...</span>\r\n                <textarea class="form-control code" ng-model="singleSelection().tempModel.content" ng-show="!apiMiddleware.apiHandler.inprocess" autofocus="autofocus"></textarea>\r\n                <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{\'close\' | translate}}</button>\r\n              <button type="submit" class="btn btn-primary" ng-show="config.allowedActions.edit" ng-disabled="apiMiddleware.apiHandler.inprocess">{{\'edit\' | translate}}</button>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="newfolder">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n        <form ng-submit="createFolder()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{\'new_folder\' | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n              <label class="radio">{{\'folder_name\' | translate}}</label>\r\n              <input class="form-control" ng-model="singleSelection().tempModel.name" autofocus="autofocus">\r\n              <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"cancel" | translate}}</button>\r\n              <button type="submit" class="btn btn-primary" ng-disabled="apiMiddleware.apiHandler.inprocess">{{\'create\' | translate}}</button>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="uploadfile">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n        <form ng-submit="uploadFiles()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{"upload_files" | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n              <label class="radio">\r\n                {{"files_will_uploaded_to" | translate}} \r\n                <b>/{{fileNavigator.currentPath.join(\'/\')}}</b>\r\n              </label>\r\n              <button class="btn btn-default btn-block" ngf-select="$parent.addForUpload($files)" ngf-multiple="true">\r\n                {{"select_files" | translate}}\r\n              </button>\r\n              \r\n              <div class="upload-list">\r\n                <ul class="list-group">\r\n                  <li class="list-group-item" ng-repeat="(index, uploadFile) in $parent.uploadFileList">\r\n                    <button class="btn btn-sm btn-danger pull-right" ng-click="$parent.removeFromUpload(index)">\r\n                        &times;\r\n                    </button>\r\n                    <h5 class="list-group-item-heading">{{uploadFile.name}}</h5>\r\n                    <p class="list-group-item-text">{{uploadFile.size | humanReadableFileSize}}</p>\r\n                  </li>\r\n                </ul>\r\n                <div ng-show="apiMiddleware.apiHandler.inprocess">\r\n                  <em>{{"uploading" | translate}}... {{apiMiddleware.apiHandler.progress}}%</em>\r\n                  <div class="progress mb0">\r\n                    <div class="progress-bar active" role="progressbar" aria-valuenow="{{apiMiddleware.apiHandler.progress}}" aria-valuemin="0" aria-valuemax="100" style="width: {{apiMiddleware.apiHandler.progress}}%"></div>\r\n                  </div>\r\n                </div>\r\n              </div>\r\n              <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <div>\r\n                  <button type="button" class="btn btn-default" data-dismiss="modal">{{"cancel" | translate}}</button>\r\n                  <button type="submit" class="btn btn-primary" ng-disabled="!$parent.uploadFileList.length || apiMiddleware.apiHandler.inprocess">{{\'upload\' | translate}}</button>\r\n              </div>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="changepermissions">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n        <form ng-submit="changePermissions()">\r\n            <div class="modal-header">\r\n              <button type="button" class="close" data-dismiss="modal">\r\n                  <span aria-hidden="true">&times;</span>\r\n                  <span class="sr-only">{{"close" | translate}}</span>\r\n              </button>\r\n              <h4 class="modal-title">{{\'change_permissions\' | translate}}</h4>\r\n            </div>\r\n            <div class="modal-body">\r\n              <table class="table mb0">\r\n                  <thead>\r\n                      <tr>\r\n                          <th>{{\'permissions\' | translate}}</th>\r\n                          <th class="col-xs-1 text-center">{{\'read\' | translate}}</th>\r\n                          <th class="col-xs-1 text-center">{{\'write\' | translate}}</th>\r\n                          <th class="col-xs-1 text-center">{{\'exec\' | translate}}</th>\r\n                      </tr>\r\n                  </thead>\r\n                  <tbody>\r\n                      <tr ng-repeat="(permTypeKey, permTypeValue) in temp.tempModel.perms">\r\n                          <td>{{permTypeKey | translate}}</td>\r\n                          <td ng-repeat="(permKey, permValue) in permTypeValue" class="col-xs-1 text-center" ng-click="main()">\r\n                              <label class="col-xs-12">\r\n                                <input type="checkbox" ng-model="temp.tempModel.perms[permTypeKey][permKey]">\r\n                              </label>\r\n                          </td>\r\n                      </tr>\r\n                </tbody>\r\n              </table>\r\n              <div class="checkbox" ng-show="config.enablePermissionsRecursive && selectionHas(\'dir\')">\r\n                <label>\r\n                  <input type="checkbox" ng-model="temp.tempModel.recursive"> {{\'recursive\' | translate}}\r\n                </label>\r\n              </div>\r\n              <div class="clearfix mt10">\r\n                  <span class="label label-primary pull-left" ng-hide="temp.multiple">\r\n                    {{\'original\' | translate}}: \r\n                    {{temp.model.perms.toCode(selectionHas(\'dir\') ? \'d\':\'-\')}} \r\n                    ({{temp.model.perms.toOctal()}})\r\n                  </span>\r\n                  <span class="label label-primary pull-right">\r\n                    {{\'changes\' | translate}}: \r\n                    {{temp.tempModel.perms.toCode(selectionHas(\'dir\') ? \'d\':\'-\')}} \r\n                    ({{temp.tempModel.perms.toOctal()}})\r\n                  </span>\r\n              </div>\r\n              <div ng-include data-src="\'error-bar\'" class="clearfix"></div>\r\n            </div>\r\n            <div class="modal-footer">\r\n              <button type="button" class="btn btn-default" data-dismiss="modal">{{"cancel" | translate}}</button>\r\n              <button type="submit" class="btn btn-primary" ng-disabled="">{{\'change\' | translate}}</button>\r\n            </div>\r\n        </form>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<div class="modal animated fadeIn" id="selector" ng-controller="ModalFileManagerCtrl">\r\n  <div class="modal-dialog">\r\n    <div class="modal-content">\r\n      <div class="modal-header">\r\n        <button type="button" class="close" data-dismiss="modal">\r\n            <span aria-hidden="true">&times;</span>\r\n            <span class="sr-only">{{"close" | translate}}</span>\r\n        </button>\r\n        <h4 class="modal-title">{{"select_destination_folder" | translate}}</h4>\r\n      </div>\r\n      <div class="modal-body">\r\n        <div>\r\n            <div ng-include="config.tplPath + \'/current-folder-breadcrumb.html\'"></div>\r\n            <div ng-include="config.tplPath + \'/main-table-modal.html\'"></div>\r\n            <hr />\r\n            <button class="btn btn-sm btn-default" ng-click="selectCurrent()">\r\n                <i class="glyphicon"></i> {{"select_this" | translate}}\r\n            </button>\r\n        </div>\r\n      </div>\r\n      <div class="modal-footer">\r\n        <button type="button" class="btn btn-default" data-dismiss="modal" ng-disabled="apiMiddleware.apiHandler.inprocess">{{"close" | translate}}</button>\r\n      </div>\r\n    </div>\r\n  </div>\r\n</div>\r\n\r\n<script type="text/ng-template" id="path-selector">\r\n  <div class="panel panel-primary mt10 mb0">\r\n    <div class="panel-body">\r\n        <div class="detail-sources">\r\n          <div class="like-code mr5"><b>{{"Selection" | translate}}:</b>  \r\n            <span ng-include="\'selected-files-msg\'"></span>\r\n          </div>\r\n        </div>\r\n        <div class="detail-sources">\r\n          <div class="like-code mr5">\r\n            <b>{{"destination" | translate}}:</b> {{ getSelectedPath() }}\r\n          </div>\r\n          <a href="" class="label label-primary" ng-click="openNavigator(fileNavigator.currentPath)">\r\n            {{\'change\' | translate}}\r\n          </a>\r\n        </div>\r\n    </div>\r\n  </div>\r\n</script>\r\n\r\n<script type="text/ng-template" id="error-bar">\r\n  <div class="label label-danger error-msg pull-left animated fadeIn" ng-show="apiMiddleware.apiHandler.error">\r\n    <i class="glyphicon glyphicon-remove-circle"></i>\r\n    <span>{{apiMiddleware.apiHandler.error}}</span>\r\n  </div>\r\n</script>\r\n\r\n<script type="text/ng-template" id="selected-files-msg">\r\n  <span ng-show="temps.length == 1">\r\n    {{singleSelection().model.name}}\r\n  </span>\r\n  <span ng-show="temps.length > 1">\r\n    {{\'these_elements\' | translate:totalSelecteds()}}\r\n    <a href="" class="label label-primary" ng-click="showDetails = !showDetails">\r\n      {{showDetails ? \'-\' : \'+\'}} {{\'details\' | translate}}\r\n    </a>\r\n  </span>\r\n  <div ng-show="temps.length > 1 &amp;&amp; showDetails">\r\n    <ul class="selected-file-details">\r\n      <li ng-repeat="tempItem in temps">\r\n        <b>{{tempItem.model.name}}</b>\r\n      </li>\r\n    </ul>\r\n  </div>\r\n</script>\r\n'), 
		e.put("src/templates/navbar.html", '<nav ng-show="config.breadcrumb" class="navbar navbar-inverse">\r\n    <div  class="container-fluid">\r\n        <div class="row">\r\n            <div class="col-sm-9 col-md-10 hidden-xs">\r\n                <div ng-show="config.breadcrumb">\r\n                    <a class="navbar-brand hidden-xs ng-binding" href="">angular-filemanager</a>\r\n                </div>\r\n                <div ng-include="config.tplPath + \'/current-folder-breadcrumb.html\'" ng-show="config.breadcrumb">\r\n                </div>\r\n            </div>\r\n            <div  ng-show="config.breadcrumb"    class="col-sm-3 col-md-2">\r\n                <div class="navbar-collapse">\r\n                    <div class="navbar-form navbar-right text-right">\r\n                        <div class="btn-group">\r\n                            <button class="btn btn-flat btn-sm dropdown-toggle" type="button" id="dropDownMenuLang" data-toggle="dropdown" aria-expanded="true">\r\n                                <i class="glyphicon glyphicon-search mr2"></i>\r\n                            </button>\r\n                            <div class="dropdown-menu animated fast fadeIn pull-right" role="menu" aria-labelledby="dropDownMenuLang">\r\n                                <input type="text" class="form-control" ng-show="config.searchForm" placeholder="{{\'search\' | translate}}..." ng-model="$parent.query">\r\n                            </div>\r\n                        </div>\r\n\r\n                        <button class="btn btn-flat btn-sm" ng-click="$parent.setTemplate(\'main-icons.html\')" ng-show="$parent.viewTemplate !==\'main-icons.html\'" title="{{\'icons\' | translate}}">\r\n                            <i class="glyphicon glyphicon-th-large"></i>\r\n                        </button>\r\n\r\n                        <button class="btn btn-flat btn-sm" ng-click="$parent.setTemplate(\'main-table.html\')" ng-show="$parent.viewTemplate !==\'main-table.html\'" title="{{\'list\' | translate}}">\r\n                            <i class="glyphicon glyphicon-th-list"></i>\r\n                        </button>\r\n\r\n                        <div class="btn-group">\r\n                            <button class="btn btn-flat btn-sm dropdown-toggle" type="button" id="dropDownMenuLang" data-toggle="dropdown" aria-expanded="true">\r\n                                <i class="glyphicon glyphicon-globe mr2"></i>\r\n                            </button>\r\n\r\n                            <ul class="dropdown-menu scrollable-menu animated fast fadeIn pull-right" role="menu" aria-labelledby="dropDownMenuLang">\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'en\')">{{"english" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'zh\')">{{"chinese" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'es\')">{{"spanish" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'pt\')">{{"portuguese" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'fr\')">{{"french" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'de\')">{{"german" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'he\')">{{"hebrew" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'sk\')">{{"slovak" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'ru\')">{{"russian" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'ua\')">{{"ukrainian" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'tr\')">{{"turkish" | translate}}</a></li>\r\n                                <li role="presentation"><a role="menuitem" tabindex="-1" href="" ng-click="changeLanguage(\'fa\')">{{"persian" | translate}}</a></li>\r\n                            </ul>\r\n                        </div>\r\n\r\n                        <div class="btn-group">\r\n                            <button class="btn btn-flat btn-sm dropdown-toggle" type="button" id="more" data-toggle="dropdown" aria-expanded="true">\r\n                                <i class="glyphicon glyphicon-option-vertical"></i>\r\n                            </button>\r\n\r\n                            <ul class="dropdown-menu scrollable-menu animated fast fadeIn pull-right" role="menu" aria-labelledby="more">\r\n                                <li role="presentation" ng-show="config.allowedActions.createFolder" ng-click="modal(\'newfolder\') && prepareNewFolder()">\r\n                                    <a href="#" role="menuitem" tabindex="-1">\r\n                                        <i class="glyphicon glyphicon-plus"></i> {{"new_folder" | translate}}\r\n                                    </a>\r\n                                </li>\r\n                                <li role="presentation" ng-show="config.allowedActions.upload" ng-click="modal(\'uploadfile\')">\r\n                                    <a href="#" role="menuitem" tabindex="-1">\r\n                                        <i class="glyphicon glyphicon-cloud-upload"></i> {{"upload_files" | translate}}\r\n                                    </a>\r\n                                </li>\r\n                            </ul>\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n            </div>\r\n        </div>\r\n    </div>\r\n</nav>'),
        e.put("src/templates/sidebar.html", '<ul class="nav nav-sidebar file-tree-root">\r\n    <li ng-repeat="item in fileNavigator.history" ng-include="\'folder-branch-item\'" ng-class="{\'active\': item.name == fileNavigator.currentPath.join(\'/\')}"></li>\r\n</ul>\r\n\r\n<script type="text/ng-template" id="folder-branch-item">\r\n    <a href="" ng-click="fileNavigator.folderClick(item.item)" class="animated fast fadeInDown fileselector">\r\n\r\n        <span class="point">\r\n            <i class="glyphicon glyphicon-chevron-down" ng-show="isInThisPath(item.name)"></i>\r\n            <i class="glyphicon glyphicon-chevron-right" ng-show="!isInThisPath(item.name)"></i>\r\n        </span>\r\n\r\n        <i class="glyphicon glyphicon-folder-open mr2" ng-show="isInThisPath(item.name)"></i>\r\n        <i class="glyphicon glyphicon-folder-close mr2" ng-show="!isInThisPath(item.name)"></i>\r\n        {{ (item.name.split(\'/\').pop() || \'/\') | strLimit : 30 }}\r\n    </a>\r\n    <ul class="nav nav-sidebar">\r\n        <li ng-repeat="item in item.nodes" ng-include="\'folder-branch-item\'" ng-class="{\'active\': item.name == fileNavigator.currentPath.join(\'/\')}"></li>\r\n    </ul>\r\n</script>'), e.put("src/templates/spinner.html", '<div class="spinner-wrapper col-xs-12">\r\n    <svg class="spinner-container" style="width:65px;height:65px" viewBox="0 0 44 44">\r\n        <circle class="path" cx="22" cy="22" r="20" fill="none" stroke-width="4"></circle>\r\n    </svg>\r\n</div>')
}]);