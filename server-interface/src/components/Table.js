import Api from "../axios/config"

const Table = ({servers, setServers}) =>{


    async function pingServer(ipAddress){
         await Api.get(`/ping/${ipAddress}`)
        .catch(err => console.log(err))
    }

    async function deleteServer(id){
         await Api.delete(`/delete/${id}`)
        .then(() =>{
            setServers(servers.filter((server) => server.id !== id))
        }).catch(err => console.log(err))
    }

    return(
        <div>
        <table className="table table-striped table-hover" id="servers">
        <thead>
            <tr>
                <th>Image</th>
                <th>IP Address</th>
                <th>Name</th>
                <th>Memory</th>
                <th>Type</th>
                <th>Status</th>
                <th>Ping</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>         
            {servers.length > 0 && servers.map((server) => (
              <tr key={server.id}>
                <td> <img src={server.imageUrl} alt={server.name} width="40" height="40"/></td>
                <td>{server.ipAddress}</td>
                <td>{server.name}</td>
                <td>{server.memory}</td>
                <td>{server.type}</td>
                <td>
                    <span className={`${server.status === "SERVER_UP" ? "badge text-bg-success": "badge text-bg-danger"}`}>{server.status === "SERVER_UP" ? "SERVER_UP" : "SERVER_DOWN"}</span>
                </td>
                <td>
                    <a href="#" onClick={() => pingServer(server.ipAddress)}>
                        <i
                            className="bi bi-bar-chart-fill" title="Ping server"></i>
                    </a>
                </td>
                <td>
                    <a href="#" name="delete" data-toggle="modal"><i className="bi bi-trash" data-toggle="tooltip"
                            title="Delete" onClick={() => deleteServer(server.id)} style={{color: 'Red'}}></i></a>
                </td> 
              </tr>
          ))}
        </tbody>
    </table>
    { servers.length === 0 && <div className="spinner-border text-info"></div>}
    </div>
    )
}

export default Table