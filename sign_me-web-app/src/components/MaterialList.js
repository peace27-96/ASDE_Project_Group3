import React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import WorkIcon from '@material-ui/icons/Work';
import CardContent from '@material-ui/core/CardContent';
import Card from '@material-ui/core/Card';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import DeleteIcon from '@material-ui/icons/Delete';
import { IconButton } from '@material-ui/core';
import BaseInstance from '../http-client/BaseInstance';
import Cookies from 'js-cookie'
import GetAppIcon from '@material-ui/icons/GetApp';
import AttachFileIcon from '@material-ui/icons/AttachFile';

export default class MaterialList extends React.Component {
    
    constructor(props) {
        super(props)
    }

    

    deleteNotice = (material) => {
        BaseInstance.post("deleteMaterial", {
            noticeDescription: material.materialPath,
            courseId : material.materialId
        }).then(res => {
            var actualMaterial = this.props.material
            var updatedMaterial = []
            for(let i = 0; i < actualMaterial.length; i++) {
                let tmp = actualMaterial[i]
                if(tmp.materialId !== material.materialId)
                    updatedMaterial.push(tmp)
            }
            
            this.props.setMaterial(updatedMaterial)
            Cookies.set("material", updatedMaterial)
        })
    }

    downloadMaterial = (path) => {
        var destination = BaseInstance.defaults.baseURL + path;
        window.location = destination
    }

    render() {
        var array = this.props.material
        return (
            <div style={{ padding: "10px" }}>
                <Card style={{ maxHeight: 250, overflow: 'auto', width: '90%' }}>
                    <CardContent>
                        {array.length > 0 ? (
                            <List>
                                {
                                    array.map(material => (
                                        <ListItem  >
                                            <ListItemIcon>
                                                <AttachFileIcon />
                                            </ListItemIcon>
                                            <ListItemText primary={`${material.description}`} /> 
                                            <IconButton onClick={()=> this.downloadMaterial(material.materialPath)}><GetAppIcon /></IconButton>
                                            {Cookies.get("lecturerId") === Cookies.get("email") ? (
                                            <IconButton onClick={() => this.deleteNotice(material)}><DeleteIcon style={{ color: "#C10000" }} /></IconButton>) 
                                            : null}
                                        </ListItem>
                                    ))
                                }
                            </List>    
                         ) : ( 
                                <ListItem>
                                    <ListItemText>No Material Available</ListItemText>
                                </ListItem>
                            )
                        }
                    </CardContent>
                </Card>
            </div>
        );
    }
}