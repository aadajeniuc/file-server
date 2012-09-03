<%--

Used to display the options at the foot of the directories view
and load plugins

--%>

<div>
    <div>
        <table>
            <form name="thisform2" method="post"
                  action="<%=root%>servlet/zipupload/<%=fileUrl%>" enctype="multipart/form-data">
                <tr>
                    <th>
                        Upload set of files(as a .zip)
                    </th>
                    <th>
                        <input type="file" name="datafile"/>
                    </th>
                    <th>
                        <input type="submit" name="Submit2" value="Upload"/>
                    </th>
                </tr>
            </form>
            <form name="thisform1" method="post"
                  action="<%=root%>servlet/upload/<%=fileUrl%>" enctype="multipart/form-data">
                <tr>
                    <th>
                        Upload file
                    </th>
                    <th>
                        <input type="file" name="datafile"/>
                    </th>
                    <th>
                        <input type="submit" name="Submit" value="Upload"/>
                    </th>
                </tr>
            </form>
        </table>

    </div>

</div>
